package com.github.attemper.common.param.executor;

import com.github.attemper.common.param.CommonParam;

import java.util.Map;

public class JobInvokingParam implements CommonParam {

    protected String id;

    protected String jobName;

    protected String triggerName;

    protected String tenantId;

    protected Map<String, Object> dataMap;

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

    public String getTenantId() {
        return tenantId;
    }

    public JobInvokingParam setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public JobInvokingParam setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }
}
