package com.github.attemper.common.result.dispatch.instance;

import lombok.ToString;

import java.util.Date;

@ToString
public class JobInstance {

    protected String id;

    protected String procInstId;

    protected String rootProcInstId;

    protected String superProcInstId;

    protected String jobName;

    protected String displayName;

    protected String triggerName;

    protected String procDefId;

    protected Date startTime;

    protected Date endTime;

    protected Long duration;

    protected int status;

    protected Integer code;

    protected String msg;

    /**
     * when you retried, the field point to the parent id
     */
    protected String parentId;

    protected String schedulerUri;

    protected String executorUri;

    protected String tenantId;

    public String getId() {
        return id;
    }

    public JobInstance setId(String id) {
        this.id = id;
        return this;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public JobInstance setProcInstId(String procInstId) {
        this.procInstId = procInstId;
        return this;
    }

    public String getRootProcInstId() {
        return rootProcInstId;
    }

    public JobInstance setRootProcInstId(String rootProcInstId) {
        this.rootProcInstId = rootProcInstId;
        return this;
    }

    public String getSuperProcInstId() {
        return superProcInstId;
    }

    public JobInstance setSuperProcInstId(String superProcInstId) {
        this.superProcInstId = superProcInstId;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public JobInstance setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public JobInstance setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public JobInstance setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public JobInstance setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public JobInstance setProcDefId(String procDefId) {
        this.procDefId = procDefId;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public JobInstance setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public JobInstance setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public JobInstance setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public JobInstance setStatus(int status) {
        this.status = status;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public JobInstance setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JobInstance setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getSchedulerUri() {
        return schedulerUri;
    }

    public JobInstance setSchedulerUri(String schedulerUri) {
        this.schedulerUri = schedulerUri;
        return this;
    }

    public String getExecutorUri() {
        return executorUri;
    }

    public JobInstance setExecutorUri(String executorUri) {
        this.executorUri = executorUri;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public JobInstance setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
