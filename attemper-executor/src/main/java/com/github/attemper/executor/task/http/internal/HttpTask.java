package com.github.attemper.executor.task.http.internal;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.app.project.ProjectInfo;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.executor.task.ParentTask;
import com.github.attemper.executor.util.CamundaUtil;
import com.github.attemper.java.sdk.common.executor.param.execution.MetaParam;
import com.github.attemper.java.sdk.common.executor.param.execution.TaskParam;
import com.github.attemper.java.sdk.common.executor.param.router.BeanParam;
import com.github.attemper.java.sdk.common.executor.param.router.RouterParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class HttpTask extends ParentTask {

    @Override
    public void executeIntern(DelegateExecution execution) {
        String jobName = CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId());
        Map<String, String> fieldMap = resolveExtensionElement(execution);
        String url = resolveUrl(execution, fieldMap, jobName);
        executeIntern(execution, url, fieldMap);
    }

    protected <V extends LogResult> V invoke(DelegateExecution execution, String url, Map<String, String> fieldMap, Class<V> v) {
        saveUrl(execution, url);
        try {
            return buildWebClient(injectTimeout(fieldMap))
                    .method(StringUtils.isBlank(fieldMap.get(REQUEST_METHOD)) ?
                            HttpMethod.POST : HttpMethod.valueOf(fieldMap.get(REQUEST_METHOD)))
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(buildParamMap(execution, fieldMap))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                    .bodyToMono(v)
                    .doOnError(Exception.class, e -> {
                        throw new RTException(3051, e);
                    })
                    .block(Duration.ofSeconds(injectTimeout(fieldMap)));
        } catch (IllegalStateException e) {
            throw new RTException(3052, e);
        }
    }

    private WebClient buildWebClient(int timeoutSeconds) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(timeoutSeconds + 120))
                                .addHandler(new WriteTimeoutHandler(timeoutSeconds + 120))));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
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
        if (fieldMap.get(SUB_URL) == null) { // router
            RouterParam routerParam = new RouterParam();
            routerParam.setBizParamMap(execution.getVariables());
            BeanParam beanParam = new BeanParam();
            beanParam.setBeanName(StringUtils.isBlank(fieldMap.get(BEAN_NAME)) ?
                    CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId())
                    : fieldMap.get(BEAN_NAME));
            beanParam.setMethodName(fieldMap.get(METHOD_NAME));
            routerParam.setBeanParam(beanParam);
            routerParam.setMetaParam(executionParam);
            appendLogText(execution, 10107, beanParam.getBeanName(), beanParam.getMethodName());
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
            appendLogText(execution, 10100);
            project = projectService.getRoot(execution.getTenantId());
        } else {
            appendLogText(execution, 10101, project.getDisplayName() + "-" + project.getProjectName());
        }
        String projectName = project.getProjectName();
        List<ProjectInfo> allProjectInfo = projectService.listInfo(projectName, execution.getTenantId());
        if (allProjectInfo.isEmpty()) {
            throw new RTException(6550);
        }

        Set<String> urls = new HashSet<>(4);  //may be one service has <=4 endpoint
        for (ProjectInfo item : allProjectInfo) {
            if (item.getUriType() == UriType.DiscoveryClient.getType()) {
                List<ServiceInstance> instances = discoveryClient.getInstances(item.getUri());
                instances.forEach(instance -> {
                    pingThenAdd(urls, instance.getUri().toString());
                });
            }
        }
        if (urls.isEmpty()) {
            for (ProjectInfo item : allProjectInfo) {
                if (item.getUriType() == UriType.IpWithPort.getType() || item.getUriType() == UriType.DomainName.getType()) {
                    pingThenAdd(urls, item.getUri());
                }
            }
        }
        if (urls.isEmpty()) {
            throw new RTException(3050);
        }
        appendLogText(execution, 10102, urls.size(), StringUtils.join(urls, CommonConstants.COMMA));
        int randomIndex = (int) (Math.random() * urls.size());
        String selectedUrl = String.valueOf(urls.toArray()[randomIndex]);
        appendLogText(execution, 10103, selectedUrl);
        String bizUrl = optimizePath(selectedUrl, fieldMap.get(SUB_URL), project.getContextPath());
        appendLogText(execution, 10104, bizUrl);
        return bizUrl;
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

    protected void afterExecution(TaskResult taskResult, DelegateExecution execution) {
        if (taskResult != null) {
            if (taskResult.getParamMap() != null && taskResult.getParamMap().size() > 0) {
                execution.setVariables(taskResult.getParamMap());
            }
            if (!taskResult.getSuccess()) {
                throw new RTException(960, StringUtils.isBlank(taskResult.getLogKey()) ?
                        taskResult.getLogText() : taskResult.getLogKey() + ":" + taskResult.getLogText());
            } else {
                if (StringUtils.isNotBlank(taskResult.getLogKey()) || StringUtils.isNotBlank(taskResult.getLogText())) {
                    String text = (taskResult.getLogText() != null && taskResult.getLogText().length() > 50000) ?
                            taskResult.getLogText().substring(0, 50000) : taskResult.getLogText();
                    appendLogText(execution, 10010, StringUtils.trimToEmpty(taskResult.getLogKey()), text);
                }
            }
        }
    }

    protected abstract void executeIntern(DelegateExecution execution, String url, Map<String, String> fieldMap);

    protected abstract String injectRouterPath();

    private static final int defTimeout = 3600;

    protected int injectTimeout(Map<String, String> fieldMap) {
        String timeoutStr = fieldMap.get(TIMEOUT);
        try {
            return Integer.parseInt(timeoutStr);
        } catch (NumberFormatException e) {
            return defTimeout;
        }
    }

    private void saveUrl(DelegateExecution execution, String url) {
        InstanceAct instanceAct = new InstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setBizUri(url);
        instanceService.updateAct(instanceAct);
    }

    protected static final String SUB_URL = "subUrl";

    protected static final String REQUEST_METHOD = "requestMethod";

    protected static final String BEAN_NAME = "beanName";

    protected static final String METHOD_NAME = "methodName";

    protected static final String TIMEOUT = "timeout";
}
