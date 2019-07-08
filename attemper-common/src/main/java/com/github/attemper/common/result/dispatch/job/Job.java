package com.github.attemper.common.result.dispatch.job;

import lombok.*;

import java.util.Date;
import java.util.List;

@ToString
public class Job {

    protected String jobName;

    protected String displayName;

    protected String jobContent;

    protected int status;

    protected int timeout;

    protected boolean concurrent;

    protected Integer maxReversion;

    protected Integer maxVersion;

    protected Integer reversion;

    protected Integer version;

    protected Date createTime;

    protected Date updateTime;

    protected Date deploymentTime;

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

    public String getJobContent() {
        return jobContent;
    }

    public Job setJobContent(String jobContent) {
        this.jobContent = jobContent;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Job setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public Job setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public Job setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    public Integer getMaxReversion() {
        return maxReversion;
    }

    public Job setMaxReversion(Integer maxReversion) {
        this.maxReversion = maxReversion;
        return this;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    public Job setMaxVersion(Integer maxVersion) {
        this.maxVersion = maxVersion;
        return this;
    }

    public Integer getReversion() {
        return reversion;
    }

    public Job setReversion(Integer reversion) {
        this.reversion = reversion;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public Job setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Job setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Job setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public Job setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
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
