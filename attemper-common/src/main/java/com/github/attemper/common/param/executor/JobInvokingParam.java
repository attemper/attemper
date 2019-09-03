package com.github.attemper.common.param.executor;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class JobInvokingParam implements CommonParam {

    protected String id;

    protected String jobName;

    protected String triggerName;

    protected List<String> beforeActIds;

    protected List<String> afterActIds;

    protected String tenantId;

    protected Map<String, Object> variableMap;

    @Override
    public String validate() {
        return null;
    }

    public String getId() {
        return id;
    }

    public JobInvokingParam setId(String id) {
        this.id = id;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public JobInvokingParam setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public JobInvokingParam setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public List<String> getBeforeActIds() {
        return beforeActIds;
    }

    public JobInvokingParam setBeforeActIds(List<String> beforeActIds) {
        this.beforeActIds = beforeActIds;
        return this;
    }

    public List<String> getAfterActIds() {
        return afterActIds;
    }

    public JobInvokingParam setAfterActIds(List<String> afterActIds) {
        this.afterActIds = afterActIds;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public JobInvokingParam setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Map<String, Object> getVariableMap() {
        return variableMap;
    }

    public JobInvokingParam setVariableMap(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
        return this;
    }
}
