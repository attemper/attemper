package com.github.attemper.executor.task.http.internal;

import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.app.project.ProjectInfo;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.executor.constant.PropertyConstants;
import com.github.attemper.executor.task.ParentTask;
import com.github.attemper.executor.util.CamundaUtil;
import com.github.attemper.java.sdk.common.executor.param.execution.MetaParam;
import com.github.attemper.java.sdk.common.executor.param.execution.TaskParam;
import com.github.attemper.java.sdk.common.executor.param.router.BeanParam;
import com.github.attemper.java.sdk.common.executor.param.router.RouterParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

public abstract class HttpTask extends ParentTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String jobName = CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId());
        Map<String, String> fieldMap = resolveExtensionElement(execution);
        String url = resolveUrl(execution, fieldMap, jobName);
        executeIntern(execution, url, fieldMap);
    }

    protected <V extends LogResult> V invoke(DelegateExecution execution, String url, Map<String, String> fieldMap, Class<V> v) {
        saveUrl(execution, url);
        int code = 3051;
        try {
            return WebClient.builder().build()
                    .method(HttpMethod.POST)
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(buildParamMap(execution, fieldMap))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                    .bodyToMono(v)
                    .doOnError(WebClientResponseException.class, e -> {
                        saveLogKey(execution, String.valueOf(code));
                        throw new RTException(code, e);
                    })
                    .block(Duration.ofSeconds(injectTimeout(fieldMap)));
        } catch (Exception e) {
            saveLogKey(execution, String.valueOf(code));
            throw new RTException(code, e);
        }
    }

    @Autowired
    protected LocalServerConfig localServerConfig;

    private Object buildParamMap(DelegateExecution execution, Map<String, String> fieldMap) {
        MetaParam executionParam = new MetaParam();
        executionParam
                .setParentActInstId(execution.getParentActivityInstanceId())
                .setProcInstId(execution.getProcessInstanceId())
                .setActId(execution.getCurrentActivityId())
                .setActName(execution.getCurrentActivityName())
                .setRequestPath(localServerConfig.getRequestPath())
                .setActInstId(execution.getActivityInstanceId())
                .setExecutionId(execution.getId());
        if (fieldMap.get(PropertyConstants.subUrl) == null) { // router
            RouterParam routerParam = new RouterParam();
            routerParam.setBizParamMap(execution.getVariables());
            BeanParam beanParam = new BeanParam();
            beanParam.setBeanName(fieldMap.get(PropertyConstants.beanName) == null ?
                    CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId())
                    : fieldMap.get(PropertyConstants.beanName));
            beanParam.setMethodName(fieldMap.get(PropertyConstants.methodName) == null ?
                    execution.getCurrentActivityId() : fieldMap.get(PropertyConstants.methodName));
            routerParam.setBeanParam(beanParam);
            routerParam.setMetaParam(executionParam);
            return routerParam;
        } else {
            TaskParam<Map<String, Object>> taskCommonParam = new TaskParam<>();
            return taskCommonParam.setMetaParam(executionParam).setBizParam(execution.getVariables());
        }
    }

    @Autowired
    protected JobService jobService;

    @Autowired
    protected ProjectService projectService;

    @Autowired
    protected DiscoveryClient discoveryClient;

    /**
     * random get a accessed url
     * @param jobName
     * @return
     */
    private String resolveUrl(DelegateExecution execution, Map<String, String> fieldMap, String jobName) {
        Project project = jobService.getProject(jobName, execution.getTenantId());
        if (project == null) {
            project = projectService.getRoot(execution.getTenantId());
        }
        String projectName = project.getProjectName();
        List<ProjectInfo> allProjectInfo = projectService.listInfo(projectName, execution.getTenantId());
        if (allProjectInfo.isEmpty()) {
            int code = 6550;
            saveLogKey(execution, String.valueOf(code));
            throw new RTException(code);
        }

        Set<String> urls = new HashSet<>(4);  //may be one service has <=4 endpoint
        for (ProjectInfo item : allProjectInfo) {
            if (item.getType() == UriType.DiscoveryClient.getType()) {
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
            saveLogKey(execution, String.valueOf(code));
            throw new RTException(code);
        }
        int randomIndex = (int) (Math.random() * urls.size());
        return optimizePath(String.valueOf(urls.toArray()[randomIndex]), fieldMap.get(PropertyConstants.subUrl), project.getContextPath());
    }

    @Autowired
    protected ToolService toolService;

    /**
     * only add the uri which is pinged successfully
     * @param urls
     * @param uri
     */
    private void pingThenAdd(Set<String> urls, String uri) {
        if (toolService.ping(uri, UriType.IpWithPort.getType())) {
            urls.add(uri);
        }
    }

    private String optimizePath(String rootUrl, String subUrl, String contextPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(rootUrl.endsWith("/") ? rootUrl.substring(0, rootUrl.length() - 1) : rootUrl);
        if (StringUtils.isNotBlank(contextPath)) {
            sb.append(contextPath.startsWith("/") ? contextPath : "/" + contextPath);
        }
        sb.append(subUrl == null ? injectRouterPath() : subUrl);
        return sb.toString();
    }

    private Map<String, String> resolveExtensionElement(DelegateExecution execution) {
        Map<String, String> map = new HashMap<>();
        Collection<ModelElementInstance> elements = execution.getBpmnModelElementInstance().getExtensionElements().getElements();
        elements.forEach(item -> {
            if (item instanceof CamundaPropertiesImpl) {
                CamundaPropertiesImpl cpi = (CamundaPropertiesImpl) item;
                cpi.getCamundaProperties().forEach(cell -> {
                    map.put(cell.getCamundaName(), cell.getCamundaValue());
                });
            }
        });
        return map;
    }

    protected void saveVariables(DelegateExecution execution, TaskResult taskResult) {
        if (taskResult.getParamMap() != null) {
            execution.setVariables(taskResult.getParamMap());
        }
        if (taskResult.getDataMap() != null) {
            execution.setVariablesLocal(taskResult.getDataMap());
        }
    }

    protected abstract void executeIntern(DelegateExecution execution, String url, Map<String, String> fieldMap);

    protected abstract String injectRouterPath();

    private static final int defTimeout = 3600;

    protected int injectTimeout(Map<String, String> fieldMap) {
        String timeoutStr = fieldMap.get(PropertyConstants.timeout);
        try {
            return Integer.parseInt(timeoutStr);
        } catch (NumberFormatException e) {
            return defTimeout;
        }
    }
}
