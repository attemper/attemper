package com.github.attemper.common.result.dispatch.delay;

import lombok.*;

import java.util.Date;
import java.util.List;

@ToString
public class DelayJob {

    protected String id;

    protected String jobName;

    protected String displayName;

    protected Integer status;

    protected List<Date> nextFireTimes;

    protected long requestTime;

    protected String tenantId;

    public String getId() {
        return id;
    }

    public DelayJob setId(String id) {
        this.id = id;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public DelayJob setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DelayJob setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public DelayJob setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public List<Date> getNextFireTimes() {
        return nextFireTimes;
    }

    public DelayJob setNextFireTimes(List<Date> nextFireTimes) {
        this.nextFireTimes = nextFireTimes;
        return this;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public DelayJob setRequestTime(long requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public DelayJob setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
