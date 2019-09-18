package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.enums.TenantStatus;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.InstanceListParam;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.java.sdk.common.result.sys.login.LoginResult;
import com.github.attemper.security.service.LoginService;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.store.SysStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
public class JobCallingService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private JobService jobService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private InstanceService instanceService;

    public void execute(String id, String jobName, String triggerName, String tenantId, Map<String, Object> dataMap) {
        this.invoke(id, jobName, triggerName, tenantId, null, null, null, dataMap);
    }

    public void manual(String id, String jobName, String tenantId, Map<String, Object> dataMap) {
        this.invoke(id, jobName, null, tenantId, null, null, null, dataMap);
    }

    public void retry(String id, String jobName, String tenantId, String parentId, List<String> beforeActIds, List<String> afterActIds, Map<String, Object> dataMap) {
        this.invoke(id, jobName, null, tenantId, parentId, beforeActIds, afterActIds, dataMap);
    }

    public void invoke(String id, String jobName, String triggerName, String tenantId, String parentId, List<String> beforeActIds, List<String> afterActIds, Map<String, Object> dataMap) {
        Tenant tenant = tenantService.get(new TenantGetParam().setUserName(tenantId));
        if (tenant == null) {
            throw new RTException(1500);
        } else if (tenant.getStatus() != TenantStatus.NORMAL.getStatus()) {
            if (log.isDebugEnabled()) {
                log.debug("tenant is not normal:{}", tenantId);
            }
            return;
        }
        Job job = jobService.get(jobName, tenantId);
        int code = -1;
        if (job == null) {
            code = 6050;
        } else if (job.getStatus() != JobStatus.ENABLED.getStatus()) {
            if (log.isDebugEnabled()) {
                log.debug("job is not enabled:{}-{}", jobName, tenantId);
            }
            return;
        } else if (!validateConcurrent(job)) {
            code = 3008;
        }
        JobInvokingParam param = new JobInvokingParam()
                .setId(id)
                .setJobName(jobName)
                .setTriggerName(triggerName)
                .setBeforeActIds(beforeActIds)
                .setAfterActIds(afterActIds)
                .setTenantId(tenantId)
                .setVariableMap(dataMap);
        if (code != -1) {
            Instance instance = buildInstance(param, null, parentId, InstanceStatus.FAILURE);
            instance.setCode(code).setMsg(StatusProperty.getValue(code))
                    .setEndTime(instance.getStartTime()).setDuration(0L);
            addInstance(instance);
            throw new RTException(code);
        }
        String baseUrl = computeUrl(param, parentId);
        String token = SysStore.getTenantTokenMap().get(tenantId);
        Instance instance = addInstance(buildInstance(param, baseUrl, parentId, InstanceStatus.RUNNING));
        try {
            invokeByWebClient(baseUrl, token, param);
        } catch (RTException rte) {
            if (rte.getCode() == HttpStatus.UNAUTHORIZED.value()) {
                invokeByWebClient(baseUrl, getToken(tenant), param);
            } else {
                instance.setCode(rte.getCode()).setMsg(rte.getMsg())
                        .setEndTime(instance.getStartTime()).setDuration(0L)
                        .setStatus(InstanceStatus.FAILURE.getStatus());
                instanceService.update(instance);
                throw rte;
            }
        }
    }

    private boolean validateConcurrent(Job job) {
        if (job.isConcurrent()) {
            return true;
        }
        int count = instanceService.count(new InstanceListParam()
                .setJobName(job.getJobName())
                .setStatus(Arrays.asList(InstanceStatus.RUNNING.getStatus())), job.getTenantId());
        return count <= 0;
    }

    @Autowired
    private ProjectService projectService;

    private String computeUrl(JobInvokingParam param, String parentId) {
        Set<String> urls = new HashSet<>();
        Project project = jobService.getProject(param.getJobName(), param.getTenantId());
        if (project == null) {
            project = projectService.getRoot(param.getTenantId());
        }
        if (project != null && project.isBindExecutor()) {
            List<String> targetExecutors = projectService.listExecutor(project.getProjectName(), param.getTenantId());
            for (String item : targetExecutors) {
                pingThenAdd(urls, item);
            }
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getExecutor().getName());
        for (ServiceInstance item : instances) {
            pingThenAdd(urls, item.getUri().toString());
        }
        if (urls.isEmpty()) {
            int code = 3005;
            Instance instance = buildInstance(param, null, parentId, InstanceStatus.FAILURE);
            instance.setEndTime(instance.getStartTime());
            instance.setDuration(0L);
            instance.setCode(code);
            instance.setMsg(StatusProperty.getValue(code));
            addInstance(instance);
            throw new RTException(code);
        }
        int randomIndex = (int) (Math.random() * urls.size());
        return urls.toArray()[randomIndex] + appProperties.getExecutor().getContextPath();
    }

    private void invokeByWebClient(String baseUrl, String token, JobInvokingParam param) {
        webClient
                .method(HttpMethod.POST)
                .uri(baseUrl + APIPath.ExecutorPath.JOB_INVOKING)
                .header(CommonConstants.token, token)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(param)
                .retrieve()
                .onStatus(s -> s == HttpStatus.UNAUTHORIZED, resp -> Mono.error(new RTException(HttpStatus.UNAUTHORIZED.value(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .doOnError(WebClientResponseException.class, err -> {
                    if (err.getMessage() != null && err.getMessage().contains("Connection refused")) {
                        throw new RTException(3009, err);
                    }
                    throw new RTException(err);
                })
                .block();
    }

    @Autowired
    private ToolService toolService;

    /**
     * only add the uri which is pinged successfully
     *
     * @param urls
     * @param uri
     */
    private void pingThenAdd(Set<String> urls, String uri) {
        if (toolService.ping(uri, UriType.IpWithPort.getType())) {
            urls.add(uri);
        }
    }

    @Autowired
    private LoginService loginService;

    private synchronized String getToken(Tenant tenant) {
        LoginResult loginResult = loginService.loginByEncoded(new LoginParam().setUserName(tenant.getUserName()).setPassword(tenant.getPassword()));
        String token = loginResult.getToken();
        SysStore.getTenantTokenMap().put(tenant.getUserName(), token);
        return token;
    }

    @Autowired
    private LocalServerConfig localServerConfig;

    private Instance buildInstance(JobInvokingParam param, String executorUri, String parentId, InstanceStatus instanceStatus) {
        return new Instance()
                .setId(param.getId())
                .setJobName(param.getJobName())
                .setTriggerName(param.getTriggerName())
                .setStartTime(new Date())
                .setStatus(instanceStatus.getStatus())
                .setSchedulerUri(localServerConfig.getRequestPath())
                .setExecutorUri(executorUri)
                .setParentId(parentId)
                .setTenantId(param.getTenantId());
    }

    private Instance addInstance(Instance instance) {
        return instanceService.add(instance);
    }
}
