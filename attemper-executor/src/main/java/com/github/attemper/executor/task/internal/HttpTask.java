package com.github.attemper.executor.task.internal;

import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.common.result.dispatch.project.ProjectInfo;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.project.ProjectService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.executor.constant.PropertyConstants;
import com.github.attemper.executor.service.instance.JobInstanceOfExeService;
import com.github.attemper.executor.util.CamundaUtil;
import com.github.attemper.java.sdk.common.executor2biz.param.BeanParam;
import com.github.attemper.java.sdk.common.executor2biz.param.ExecutionParam;
import com.github.attemper.java.sdk.common.executor2biz.param.RouterParam;
import com.github.attemper.java.sdk.common.executor2biz.param.TaskExecutionParam;
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
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.*;

@Slf4j
public abstract class HttpTask implements JavaDelegate {

    protected HttpMethod requestMethod;

    protected String subUrl;

    protected String beanName;

    protected String methodName;

    @Override
    public void execute(DelegateExecution execution) {
        String jobName = CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId());
        JobService jobService = SpringContextAware.getBean(JobService.class);
        Job job = jobService.get(jobName, execution.getTenantId());
        resolveExtensionElement(execution);
        String url = resolveUrl(jobName, execution.getTenantId()) + (subUrl == null ? injectRouterPath() : subUrl);
        saveBizUrl(execution, url);
        WebClient webClient = buildWebClient(url, job);
        executeIntern(webClient, execution);
    }

    protected <V extends LogResult> V invoke(WebClient webClient, DelegateExecution execution, Class<V> v) {
        return webClient
                .method(this.requestMethod)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(buildParamMap(execution))
                .retrieve()
                .onStatus(e -> e.is4xxClientError(), resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(v)
                .doOnError(WebClientResponseException.class, err -> {
                    throw new RTException(500);
                })
                .block();
    }

    private Object buildParamMap(DelegateExecution execution) {
        ExecutionParam executionParam = new ExecutionParam();
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
            routerParam.setExecutionParam(executionParam);
            return routerParam;
        } else {
            TaskExecutionParam<Map<String, Object>> taskCommonParam = new TaskExecutionParam<>();
            return taskCommonParam.setExecutionParam(executionParam).setBizParam(execution.getVariables());
        }
    }

    private WebClient buildWebClient(String url, Job job) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(job.getTimeout()))));
        return WebClient.builder().baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    /**
     * random get a accessed url
     * @param jobName
     * @return
     */
    private String resolveUrl(String jobName, String tenantId) {
        JobService jobService = SpringContextAware.getBean(JobService.class);
        ProjectService projectService = SpringContextAware.getBean(ProjectService.class);
        Project project = jobService.getProject(jobName, tenantId);
        if (project == null) {
            project = projectService.getRoot(tenantId);
        }
        String projectName = project.getProjectName();
        List<ProjectInfo> allProjectInfo = projectService.listInfo(projectName, tenantId);
        if (allProjectInfo.isEmpty()) {
            throw new RTException(500);
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
            throw new RTException(500);
        }
        int randomIndex = (int) (Math.random() * urls.size());
        return urls.toArray()[randomIndex] + optimizeContextPath(project.getContextPath());
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

    private String optimizeContextPath(String contextPath) {
        if (StringUtils.isBlank(contextPath)) {
            return "/";
        }
        contextPath = contextPath.trim();
        return contextPath.startsWith("/") ? contextPath : "/" + contextPath;
    }

    private void resolveExtensionElement(DelegateExecution execution) {
        Collection<ModelElementInstance> elements = execution.getBpmnModelElementInstance().getExtensionElements().getElements();
        elements.forEach(item -> {
            if (item instanceof CamundaPropertiesImpl) {
                CamundaPropertiesImpl cpi = (CamundaPropertiesImpl) item;
                cpi.getCamundaProperties().forEach(cell -> {
                    String camundaValue = cell.getCamundaValue();
                    if (camundaValue != null) {
                        if (PropertyConstants.requestMethod.equals(cell.getCamundaName())) {
                            this.requestMethod = HttpMethod.valueOf(camundaValue.toUpperCase());
                        } else if (PropertyConstants.subUrl.equals(cell.getCamundaName())) {
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
        if (this.requestMethod == null) {
            this.requestMethod = HttpMethod.POST;
        }
    }

    private void saveBizUrl(DelegateExecution execution, String url) {
        JobInstanceOfExeService jobInstanceOfExeService = SpringContextAware.getBean(JobInstanceOfExeService.class);
        JobInstanceAct jobInstanceAct = JobInstanceAct.builder().actInstId(execution.getActivityInstanceId()).bizUri(url).build();
        jobInstanceOfExeService.updateAct(jobInstanceAct);
    }

    protected abstract void executeIntern(WebClient webClient, DelegateExecution execution);

    protected abstract String injectRouterPath();

}
