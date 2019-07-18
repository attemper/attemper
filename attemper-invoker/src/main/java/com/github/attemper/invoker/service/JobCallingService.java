package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.JobInstanceListParam;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.project.ProjectService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.java.sdk.common.result.sys.login.LoginResult;
import com.github.attemper.security.service.LoginService;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.store.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
    private JobInstanceService jobInstanceService;

    public void execute(String id, String jobName, String triggerName, String tenantId, Map<String, Object> dataMap) {
        this.invoke(id, jobName, triggerName, tenantId, null, dataMap);
    }

    public void manual(String id, String jobName, String tenantId, Map<String, Object> dataMap) {
        this.invoke(id, jobName, null, tenantId, null, dataMap);
    }

    public void retry(String id, String jobName, String tenantId, String parentId, Map<String, Object> dataMap) {
        this.invoke(id, jobName, null, tenantId, parentId, dataMap);
    }

    public void invoke(String id, String jobName, String triggerName, String tenantId, String parentId, Map<String, Object> dataMap) {
        JobInvokingParam param = new JobInvokingParam()
                .setId(id)
                .setJobName(jobName)
                .setTriggerName(triggerName)
                .setTenantId(tenantId)
                .setDataMap(dataMap);
        Job job = jobService.get(jobName, tenantId);
        int code = -1;
        if (job == null) {
            code = 6050;
        } else if (job.getStatus() != JobStatus.ENABLED.getStatus()) {
            code = 3010;
        } else if (!validateConcurrent(job)) {
            code = 3008;
        }
        if (code != -1) {
            JobInstance jobInstance = buildInstance(param, null, parentId, JobInstanceStatus.FAILURE);
            jobInstance.setEndTime(jobInstance.getStartTime());
            jobInstance.setDuration(0L);
            jobInstance.setCode(code);
            jobInstance.setMsg(StatusProperty.getValue(code));
            saveInstance(jobInstance);
            throw new RTException(code);
        }
        String baseUrl = computeUrl(param, parentId);
        String token = Store.getTenantTokenMap().get(tenantId);
        saveInstance(buildInstance(param, baseUrl, parentId, JobInstanceStatus.RUNNING));
        try {
            invokeByWebClient(baseUrl, token, param);
        } catch (RTException e) {
            invokeByWebClient(baseUrl, getToken(tenantId), param);
        }
    }

    private boolean validateConcurrent(Job job) {
        if (job.isConcurrent()) {
            return true;
        }
        int count = jobInstanceService.count(new JobInstanceListParam()
                .setJobName(job.getJobName())
                .setStatus(Arrays.asList(JobInstanceStatus.RUNNING.getStatus())), job.getTenantId());
        if (count <= 0) {
            return true;
        }
        return false;
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
            JobInstance jobInstance = buildInstance(param, null, parentId, JobInstanceStatus.FAILURE);
            jobInstance.setEndTime(jobInstance.getStartTime());
            jobInstance.setDuration(0L);
            jobInstance.setCode(code);
            jobInstance.setMsg(StatusProperty.getValue(code));
            saveInstance(jobInstance);
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

    @Autowired
    private TenantService tenantService;

    private synchronized String getToken(String tenantId) {
        Tenant tenant = tenantService.get(new TenantGetParam().setUserName(tenantId));
        LoginResult loginResult = loginService.loginByEncoded(new LoginParam().setUserName(tenantId).setPassword(tenant.getPassword()));
        String token = loginResult.getToken();
        Store.getTenantTokenMap().put(tenantId, token);
        return token;
    }

    @Autowired
    private LocalServerConfig localServerConfig;

    private JobInstance buildInstance(JobInvokingParam param, String executorUri, String parentId, JobInstanceStatus jobInstanceStatus) {
        return new JobInstance()
                .setId(param.getId())
                .setJobName(param.getJobName())
                .setTriggerName(param.getTriggerName())
                .setStartTime(new Date())
                .setStatus(jobInstanceStatus.getStatus())
                .setSchedulerUri(localServerConfig.getRequestPath())
                .setExecutorUri(executorUri)
                .setParentId(parentId)
                .setTenantId(param.getTenantId());
    }

    private void saveInstance(JobInstance jobInstance) {
        jobInstanceService.add(jobInstance);
    }
}
