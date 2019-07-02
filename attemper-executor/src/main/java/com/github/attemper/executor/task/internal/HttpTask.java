package com.github.attemper.executor.task.internal;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.common.result.dispatch.project.ProjectInfo;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.project.ProjectService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.executor.constant.PropertyConstants;
import com.github.attemper.executor.util.CamundaUtil;
import com.github.attemper.java.sdk.common.executor.param.execution.MetaParam;
import com.github.attemper.java.sdk.common.executor.param.execution.TaskParam;
import com.github.attemper.java.sdk.common.executor.param.router.BeanParam;
import com.github.attemper.java.sdk.common.executor.param.router.RouterParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.*;

@Slf4j
public abstract class HttpTask implements JavaDelegate {

    protected String subUrl;

    protected String beanName;

    protected String methodName;

    @Override
    public void execute(DelegateExecution execution) {
        String jobName = CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId());
        JobService jobService = SpringContextAware.getBean(JobService.class);
        Job job = jobService.get(jobName, execution.getTenantId());
        resolveExtensionElement(execution);
        String url = resolveUrl(jobName, execution);
        executeIntern(execution, job, url);
    }

    protected <V extends LogResult> V invoke(DelegateExecution execution, Job job, String url, Class<V> v) {
        WebClient webClient = buildWebClient(job);
        return webClient
                .method(HttpMethod.POST)
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(buildParamMap(execution))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(v)
                .doOnError(WebClientResponseException.class, err -> {
                    int code = 3051;
                    saveInstanceAct(execution, url, String.valueOf(code), StatusProperty.getValue(code) + ":" + err.getStatusCode().getReasonPhrase(), JobInstanceStatus.FAILURE);
                    throw new RTException(code, err);
                })
                .block();
    }

    private Object buildParamMap(DelegateExecution execution) {
        MetaParam executionParam = new MetaParam();
        executionParam
                .setParentActInstId(execution.getParentActivityInstanceId())
                .setExecutionId(execution.getId())
                .setProcInstId(execution.getProcessInstanceId())
                .setActId(execution.getCurrentActivityId())
                .setActName(execution.getCurrentActivityName())
                .setRequestPath(SpringContextAware.getBean(LocalServerConfig.class).getRequestPath())
                .setActInstId(execution.getActivityInstanceId());
        if (subUrl == null) { // router
            RouterParam routerParam = new RouterParam();
            routerParam.setBizParamMap(execution.getVariables());
            BeanParam beanParam = new BeanParam();
            beanParam.setBeanName(beanName == null ?
                    CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId()) : beanName);
            beanParam.setMethodName(methodName == null ? execution.getCurrentActivityId() : methodName);
            routerParam.setBeanParam(beanParam);
            routerParam.setMetaParam(executionParam);
            return routerParam;
        } else {
            TaskParam<Map<String, Object>> taskCommonParam = new TaskParam<>();
            return taskCommonParam.setMetaParam(executionParam).setBizParam(execution.getVariables());
        }
    }

    private WebClient buildWebClient(Job job) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(job.getTimeout()))));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    /**
     * random get a accessed url
     * @param jobName
     * @return
     */
    private String resolveUrl(String jobName, DelegateExecution execution) {
        JobService jobService = SpringContextAware.getBean(JobService.class);
        ProjectService projectService = SpringContextAware.getBean(ProjectService.class);
        Project project = jobService.getProject(jobName, execution.getTenantId());
        if (project == null) {
            project = projectService.getRoot(execution.getTenantId());
        }
        String projectName = project.getProjectName();
        List<ProjectInfo> allProjectInfo = projectService.listInfo(projectName, execution.getTenantId());
        if (allProjectInfo.isEmpty()) {
            int code = 6550;
            saveInstanceAct(execution, null, String.valueOf(code), StatusProperty.getValue(code), JobInstanceStatus.FAILURE);
            throw new RTException(code);
        }

        Set<String> urls = new HashSet<>(4);  //may be one service has <=4 endpoint
        for (ProjectInfo item : allProjectInfo) {
            if (item.getType() == UriType.DiscoveryClient.getType()) {
                DiscoveryClient discoveryClient = SpringContextAware.getBean(DiscoveryClient.class);
                List<ServiceInstance> instances = discoveryClient.getInstances(item.getUri());
                instances.forEach(instance -> {
                    pingThenAdd(urls, instance.getUri().toString());
                });
            }
        }
        if (urls.isEmpty()) {
            for (ProjectInfo item : allProjectInfo) {
                if (item.getType() == UriType.IpWithPort.getType() || item.getType() == UriType.DomainName.getType()) {
                    pingThenAdd(urls, item.getUri());
                }
            }
        }
        if (urls.isEmpty()) {
            int code = 3050;
            saveInstanceAct(execution, null, String.valueOf(code), StatusProperty.getValue(code), JobInstanceStatus.FAILURE);
            throw new RTException(code);
        }
        int randomIndex = (int) (Math.random() * urls.size());
        return optimizePath(String.valueOf(urls.toArray()[randomIndex]), project.getContextPath());
    }

    /**
     * only add the uri which is pinged successfully
     * @param urls
     * @param uri
     */
    private void pingThenAdd(Set<String> urls, String uri) {
        if (SpringContextAware.getBean(ToolService.class).ping(uri, UriType.IpWithPort.getType())) {
            urls.add(uri);
        }
    }

    private String optimizePath(String rootUrl, String contextPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(rootUrl.endsWith("/") ? rootUrl.substring(0, rootUrl.length() - 1) : rootUrl);
        if (StringUtils.isNotBlank(contextPath)) {
            sb.append(contextPath.startsWith("/") ? contextPath : "/" + contextPath);
        }
        sb.append(subUrl == null ? injectRouterPath() : subUrl);
        return sb.toString();
    }

    private void resolveExtensionElement(DelegateExecution execution) {
        Collection<ModelElementInstance> elements = execution.getBpmnModelElementInstance().getExtensionElements().getElements();
        elements.forEach(item -> {
            if (item instanceof CamundaPropertiesImpl) {
                CamundaPropertiesImpl cpi = (CamundaPropertiesImpl) item;
                cpi.getCamundaProperties().forEach(cell -> {
                    String camundaValue = cell.getCamundaValue();
                    if (camundaValue != null) {
                        if (PropertyConstants.subUrl.equals(cell.getCamundaName())) {
                            this.subUrl = camundaValue.startsWith("/") ? camundaValue : "/" + camundaValue;
                        } else if (PropertyConstants.beanName.equals(cell.getCamundaName())) {
                            this.beanName = camundaValue;
                        } else if (PropertyConstants.methodName.equals(cell.getCamundaName())) {
                            this.methodName = camundaValue;
                        }
                    }
                });
            }
        });
    }

    protected void saveInstanceAct(DelegateExecution execution, String url, String logKey, String logText, JobInstanceStatus jobInstanceStatus) {
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstanceAct jobInstanceAct = jobInstanceService.getAct(execution.getActivityInstanceId());
        Date now = new Date();
        jobInstanceAct.setBizUri(url);
        jobInstanceAct.setEndTime(now);
        jobInstanceAct.setDuration(now.getTime() - jobInstanceAct.getStartTime().getTime());
        jobInstanceAct.setLogKey(logKey);
        jobInstanceAct.setLogText(logText);
        jobInstanceAct.setStatus(jobInstanceStatus.getStatus());
        jobInstanceService.updateAct(jobInstanceAct);

        if (jobInstanceStatus != null && JobInstanceStatus.FAILURE.getStatus() == jobInstanceStatus.getStatus()) {
            JobInstance jobInstance = jobInstanceService.get(execution.getBusinessKey());
            jobInstance.setEndTime(now);
            jobInstance.setDuration(now.getTime() - jobInstance.getStartTime().getTime());
            jobInstance.setStatus(JobInstanceStatus.FAILURE.getStatus());
            int code;
            try {
                code = Integer.parseInt(logKey);
                jobInstance.setMsg(logText);
            } catch (Exception e) {
                code = CommonConstants.INTERNAL_SERVER_ERROR;
                jobInstance.setMsg(logText + "\n" + e.getMessage());
            }
            jobInstance.setCode(code);
            jobInstanceService.update(jobInstance);
        }
    }

    protected abstract void executeIntern(DelegateExecution execution, Job job, String url);

    protected abstract String injectRouterPath();

}
