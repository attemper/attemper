package com.sse.attemper.executor.task.internal;

import com.sse.attemper.common.enums.UriType;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.common.result.dispatch.project.ProjectInfo;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.service.tool.ToolService;
import com.sse.attemper.executor.constant.PropertyConstants;
import com.sse.attemper.executor.service.operate.JobOfExeService;
import com.sse.attemper.executor.service.operate.ProjectOfExeService;
import com.sse.attemper.executor.util.CamundaUtil;
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
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class HttpTask implements JavaDelegate {

    protected HttpMethod requestMethod;

    protected String subUrl;

    @Override
    public void execute(DelegateExecution execution) {
        String jobName = CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId());
        String url = resolveUrl(jobName, execution.getTenantId());
        resolveExtensionElement(jobName, execution);
        executeWithUrl(url, jobName, execution);
    }

    protected abstract void executeWithUrl(String url, String jobName, DelegateExecution execution);

    protected abstract Map<String, Object> buildParamMap(DelegateExecution execution);

    protected String invoke(WebClient webClient, DelegateExecution execution) {
        return webClient
                .method(this.requestMethod)
                .uri(this.subUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(buildParamMap(execution))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    protected WebClient buildWebClient(String url, int readTimeout) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(readTimeout))));
        return WebClient.builder().baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    /**
     * random get a accessed url
     * @param jobName
     * @return
     */
    private String resolveUrl(String jobName, String tenantId) {
        JobOfExeService jobOfExeService = ContextBeanAware.getBean(JobOfExeService.class);
        ProjectOfExeService projectOfExeService = ContextBeanAware.getBean(ProjectOfExeService.class);
        Project project = jobOfExeService.getProject(jobName, tenantId);
        if (project == null) {
            // find root
            List<Project> allProjects = projectOfExeService.getAll(tenantId);
            if (allProjects.isEmpty()) {
                throw new RTException(500);
            } else {
                for (Project item : allProjects) {
                    if (StringUtils.isBlank(item.getParentProjectName())) {
                        // find root already
                        project = item;
                        break;
                    }
                }
            }
        }
        if (project == null) {
            throw new RTException(500);
        }
        String projectName = project.getProjectName();
        List<ProjectInfo> allProjectInfos = projectOfExeService.listInfos(projectName, tenantId);
        if (allProjectInfos.isEmpty()) {
            throw new RTException(500);
        }

        List<String> urls = new ArrayList<>(4);  //may be one service has <=4 endpoint
        for (ProjectInfo item : allProjectInfos) {
            if (item.getType() == UriType.DiscoveryClient.getType()) {
                DiscoveryClient discoveryClient = ContextBeanAware.getBean(DiscoveryClient.class);
                List<ServiceInstance> instances = discoveryClient.getInstances(item.getUri());
                if (instances.isEmpty()) {
                    throw new RTException(500);
                } else {
                    instances.forEach(instance -> {
                        try {
                            pingThenAdd(urls, instance.getUri().toString());
                        } catch (RTException e) {
                            log.error(e.getMsg(), e);
                        }
                    });
                }
            }
        }
        if (urls.isEmpty()) {
            for (ProjectInfo item : allProjectInfos) {
                if (item.getType() == UriType.IpWithPort.getType() || item.getType() == UriType.DomainName.getType()) {
                    pingThenAdd(urls, item.getUri());
                } else {
                    throw new RTException(500);
                }
            }
        }
        if (urls.isEmpty()) {
            throw new RTException(500);
        }
        int randomIndex = (int) (Math.random() * urls.size());
        String appUrl = urls.get(randomIndex) + optimizeContextPath(project.getContextPath());
        return appUrl;
    }

    /**
     * only add the uri which is pinged successfully
     * @param urls
     * @param uri
     */
    private void pingThenAdd(List<String> urls, String uri) {
        if (ContextBeanAware.getBean(ToolService.class).ping(uri, UriType.IpWithPort.getType())) {
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

    private void resolveExtensionElement(String jobName, DelegateExecution execution) {
        Collection<ModelElementInstance> elements = execution.getBpmnModelElementInstance().getExtensionElements().getElements();
        elements.forEach(item -> {
            CamundaPropertiesImpl cpi = (CamundaPropertiesImpl) item;
            cpi.getCamundaProperties().forEach(cell -> {
                if (cell.getCamundaValue() != null) {
                    if (PropertyConstants.requestMethod.equals(cell.getCamundaName())) {
                        this.requestMethod = HttpMethod.valueOf(cell.getCamundaValue().toUpperCase());
                    } else if (PropertyConstants.subUrl.equals(cell.getCamundaName())) {
                        this.subUrl = cell.getCamundaValue().startsWith("/") ? cell.getCamundaValue() : "/" + cell.getCamundaValue();
                    }
                }
            });
        });
        if (this.requestMethod == null) {
            this.requestMethod = HttpMethod.POST;
        }
        if (this.subUrl == null) {
            this.subUrl = "/" + jobName + "/" + execution.getCurrentActivityId();
        }
    }
}
