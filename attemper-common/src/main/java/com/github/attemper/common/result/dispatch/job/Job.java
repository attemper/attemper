package com.github.attemper.common.result.dispatch.job;

import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
public class Job {

    protected String jobName;

    protected String displayName;

    protected String content;

    protected int status;

    protected boolean concurrent;

    protected Date updateTime;

    protected List<Date> nextFireTimes;

    protected String remark;

    protected String tenantId;

    public String getJobName() {
        return jobName;
    }

    public Job setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Job setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Job setContent(String content) {
        this.content = content;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Job setStatus(int status) {
        this.status = status;
        return this;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public Job setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Job setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public List<Date> getNextFireTimes() {
        return nextFireTimes;
    }

    public Job setNextFireTimes(List<Date> nextFireTimes) {
        this.nextFireTimes = nextFireTimes;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public Job setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Job setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
