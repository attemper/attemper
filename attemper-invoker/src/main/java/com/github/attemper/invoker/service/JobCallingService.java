package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.enums.TenantStatus;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.InstanceListParam;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.param.sys.tenant.TenantNameParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.core.ext.condition.ConditionStrategyService;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class JobCallingService {

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

    @Autowired
    private RetryHttpHandler httpHandler;

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
        Map<String, Object> variableMap = jobService.getArgsAsMap(jobName, tenantId);
        if (dataMap != null) {
            variableMap.putAll(dataMap);
        }
        JobInvokingParam param = new JobInvokingParam()
                .setId(id)
                .setJobName(jobName)
                .setTriggerName(triggerName)
                .setBeforeActIds(beforeActIds)
                .setAfterActIds(afterActIds)
                .setTenantId(tenantId)
                .setVariableMap(variableMap);
        int code = -1;
        Tenant tenant = tenantService.get(new TenantNameParam().setUserName(tenantId));
        if (tenant == null) {
            throw new RTException(1500);
        } else if (tenant.getStatus() != TenantStatus.NORMAL.getStatus()) {
            if (log.isDebugEnabled()) {
                log.debug("tenant is not normal:{}", tenantId);
            }
            return;
        }
        Job job = jobService.get(jobName, tenantId);
        if (job == null) {  // exist?
            code = 6050;
        } else if (job.getStatus() != JobStatus.ENABLED.getStatus()) { // enabled?
            if (log.isDebugEnabled()) {
                log.debug("job is not enabled:{}-{}", jobName, tenantId);
            }
            return;
        } else if (!validateConcurrent(job)) { // can't concurrent but has a running instance?
            code = 3008;
        }
        try {
            boolean conditionValidation = this.validateCondition(job, variableMap);
            if (!conditionValidation) {
                code = 931;
                Instance instance = buildInstance(param, job.getDisplayName(), null, parentId, InstanceStatus.UNMET);
                instance.setCode(code).setMsg(StatusProperty.getValue(code))
                        .setEndTime(instance.getStartTime()).setDuration(0L);
                addInstance(instance);
                return;
            }
        } catch (Exception e) {
            Instance instance = buildInstance(param, job.getDisplayName(), null, parentId, InstanceStatus.FAILURE);
            if (e instanceof RTException) {
                RTException rte = (RTException) e;
                instance.setCode(rte.getCode()).setMsg(rte.getMsg());
            } else {
                code = 932;
                instance.setCode(code).setMsg(StatusProperty.getValue(code) + ":" + e.getMessage());
                log.error(instance.getCode() + ":" + instance.getMsg(), e);
            }
            instance.setEndTime(instance.getStartTime()).setDuration(0L);
            addInstance(instance);
            return;
        }
        if (code != -1) {
            Instance instance = buildInstance(param, job.getDisplayName(), null, parentId, InstanceStatus.FAILURE);
            instance.setCode(code).setMsg(StatusProperty.getValue(code))
                    .setEndTime(instance.getStartTime()).setDuration(0L);
            addInstance(instance);
            throw new RTException(code);
        }
        String baseUrl = computeUrl(param, job.getDisplayName(), parentId);
        String token = SysStore.getTenantTokenMap().get(tenantId);
        Instance instance = addInstance(buildInstance(param, job.getDisplayName(), baseUrl, parentId, InstanceStatus.RUNNING));
        try {
            httpHandler.invokeByWebClient(baseUrl, token, param);
        } catch (Exception e) {
            if (e instanceof RTException) {
                RTException rte = (RTException) e;
                if (rte.getCode() == HttpStatus.UNAUTHORIZED.value()) {
                    httpHandler.invokeByWebClient(baseUrl, getToken(tenant), param);
                } else {
                    instance.setCode(rte.getCode()).setMsg(rte.getMsg())
                            .setEndTime(instance.getStartTime()).setDuration(0L)
                            .setStatus(InstanceStatus.FAILURE.getStatus());
                    instanceService.update(instance);
                }
            } else {
                instance.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(e.getMessage())
                        .setEndTime(instance.getStartTime()).setDuration(0L)
                        .setStatus(InstanceStatus.FAILURE.getStatus());
                log.error(instance.getCode() + ":" + instance.getMsg(), e);
                instanceService.update(instance);
            }
        }
    }

    @Autowired
    private ConditionStrategyService conditionStrategyService;

    private boolean validateCondition(Job job, Map<String, Object> variableMap) {
        // validate condition
        List<Condition> conditions = jobService.getConditions(job.getJobName(), job.getTenantId());
        if (conditions.size() == 0) {
            return true;
        }
        boolean result = true;
        for (Condition condition : conditions) {
            if (result && !conditionStrategyService.get(condition.getConditionType()).validate(condition, variableMap)) {
                result = false;
                break;
            }
        }
        return result && !(job.getOnce() > 0 && hasExecutedToday(job));
    }

    private boolean hasExecutedToday(Job job) {
        return instanceService.countInstance(buildInstanceMap(job)) > 0;
    }

    private Map<String, Object> buildInstanceMap(Job job) {
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put(CommonConstants.jobName, job.getJobName());
        paramMap.put(CommonConstants.tenantId, job.getTenantId());
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        paramMap.put(CommonConstants.lowerStartTime, cal1.getTime());
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        cal2.set(Calendar.MINUTE, 59);
        cal2.set(Calendar.SECOND, 59);
        paramMap.put(CommonConstants.upperStartTime, cal2.getTime());
        paramMap.put(CommonConstants.status, Arrays.asList(InstanceStatus.SUCCESS.getStatus()));
        return paramMap;
    }

    private boolean validateConcurrent(Job job) {
        if (job.getConcurrent() > 0) {
            return true;
        }
        int count = instanceService.count(new InstanceListParam()
                .setJobName(job.getJobName())
                .setStatus(Arrays.asList(InstanceStatus.RUNNING.getStatus())), job.getTenantId());
        return count <= 0;
    }

    @Autowired
    private ProjectService projectService;

    private String computeUrl(JobInvokingParam param, String displayName, String parentId) {
        Set<String> urls = new HashSet<>();
        Project project = jobService.getProject(param.getJobName(), param.getTenantId());
        if (project == null) {
            project = projectService.getRoot(param.getTenantId());
        }
        if (project != null && project.getBindExecutor() > 0) {
            List<String> targetExecutors = projectService.listExecutor(project.getProjectName(), param.getTenantId());
            for (String item : targetExecutors) {
                pingThenAdd(urls, item);
            }
        } else {
            List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getExecutor().getName());
            for (ServiceInstance item : instances) {
                pingThenAdd(urls, item.getUri().toString());
            }
        }
        if (urls.isEmpty()) {
            int code = 3005;
            Instance instance = buildInstance(param, displayName, null, parentId, InstanceStatus.FAILURE);
            instance.setCode(code).setMsg(StatusProperty.getValue(code)).setEndTime(instance.getStartTime()).setDuration(0L);
            addInstance(instance);
            throw new RTException(code);
        }
        if (urls.size() == 1) {
            return urls.toArray()[0].toString();
        }
        int randomIndex = (int) (Math.random() * urls.size());
        return urls.toArray()[randomIndex].toString();
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

    private Instance buildInstance(JobInvokingParam param, String displayName, String executorUri, String parentId, InstanceStatus instanceStatus) {
        return new Instance()
                .setId(param.getId())
                .setJobName(param.getJobName())
                .setDisplayName(displayName)
                .setTriggerName(param.getTriggerName())
                .setStartTime(System.currentTimeMillis())
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
