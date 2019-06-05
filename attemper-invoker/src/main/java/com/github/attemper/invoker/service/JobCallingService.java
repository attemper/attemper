package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
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
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class JobCallingService {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private WebClient webClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    public void invoke(String jobName, String tenantId) {
        this.invoke(jobName, null, tenantId);
    }

    public void invoke(String jobName, String triggerName, String tenantId) {
        JobInvokingParam param = JobInvokingParam.builder()
                .id(idGenerator.getNextId())
                .jobName(jobName)
                .triggerName(triggerName)
                .tenantId(tenantId)
                .build();
        String baseUrl = computeUrl(param);
        String token = Store.getTenantTokenMap().get(tenantId);
        saveInstance(buildInstance(param, baseUrl, JobInstanceStatus.RUNNING));
        try {
            invokeByWebClient(baseUrl, token, param);
        } catch (RTException e) {
            invokeByWebClient(baseUrl, getToken(tenantId), param);
        }
    }

    @Autowired
    private JobService jobService;

    @Autowired
    private ProjectService projectService;

    private String computeUrl(JobInvokingParam param) {
        Set<String> urls = new HashSet<>();
        Project project = jobService.getProject(param.getJobName(), param.getTenantId());
        if (project == null) {
            project = projectService.getRoot(param.getTenantId());
        }
        if (project != null && project.getBindExecutor() != null && project.getBindExecutor()) {
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
            JobInstance jobInstance = buildInstance(param, null, JobInstanceStatus.FAILURE);
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
        Tenant tenant = tenantService.get(TenantGetParam.builder().userName(tenantId).build());
        LoginResult loginResult = loginService.login(new LoginParam().setUserName(tenantId).setPassword(tenant.getPassword()));
        String token = loginResult.getToken();
        Store.getTenantTokenMap().put(tenantId, token);
        return token;
    }

    @Autowired
    private LocalServerConfig localServerConfig;

    private JobInstance buildInstance(JobInvokingParam param, String executorUri, JobInstanceStatus jobInstanceStatus) {
        return JobInstance.builder()
                .id(param.getId())
                .jobName(param.getJobName())
                .triggerName(param.getTriggerName())
                .startTime(new Date())
                .status(jobInstanceStatus.getStatus())
                .schedulerUri(localServerConfig.getRequestPath())
                .executorUri(executorUri)
                .tenantId(param.getTenantId())
                .build();
    }

    @Autowired
    private JobInstanceService jobInstanceService;

    private void saveInstance(JobInstance jobInstance) {
        jobInstanceService.add(jobInstance);
    }
}
