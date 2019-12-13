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

    protected int concurrent;

    protected int once;

    protected Long updateTime;

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

    public int getConcurrent() {
        return concurrent;
    }

    public Job setConcurrent(int concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    public int getOnce() {
        return once;
    }

    public Job setOnce(int once) {
        this.once = once;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public Job setUpdateTime(Long updateTime) {
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
