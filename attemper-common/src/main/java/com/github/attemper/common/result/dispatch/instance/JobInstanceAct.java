package com.github.attemper.common.result.dispatch.instance;

import lombok.ToString;

import java.util.Date;

@ToString
public class JobInstanceAct {

    protected String id;

    protected String actInstId;

    protected String parentActInstId;

    protected String executionId;

    protected String procInstId;

    protected String rootProcInstId;

    protected String actId;

    protected String actName;

    protected String actType;

    protected Date startTime;

    protected Date endTime;

    protected Long duration;

    protected int status;

    protected String logKey;

    protected String logText;

    protected String bizUri;

    public String getId() {
        return id;
    }

    public JobInstanceAct setId(String id) {
        this.id = id;
        return this;
    }

    public String getActInstId() {
        return actInstId;
    }

    public JobInstanceAct setActInstId(String actInstId) {
        this.actInstId = actInstId;
        return this;
    }

    public String getParentActInstId() {
        return parentActInstId;
    }

    public JobInstanceAct setParentActInstId(String parentActInstId) {
        this.parentActInstId = parentActInstId;
        return this;
    }

    public String getExecutionId() {
        return executionId;
    }

    public JobInstanceAct setExecutionId(String executionId) {
        this.executionId = executionId;
        return this;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public JobInstanceAct setProcInstId(String procInstId) {
        this.procInstId = procInstId;
        return this;
    }

    public String getRootProcInstId() {
        return rootProcInstId;
    }

    public JobInstanceAct setRootProcInstId(String rootProcInstId) {
        this.rootProcInstId = rootProcInstId;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public JobInstanceAct setActId(String actId) {
        this.actId = actId;
        return this;
    }

    public String getActName() {
        return actName;
    }

    public JobInstanceAct setActName(String actName) {
        this.actName = actName;
        return this;
    }

    public String getActType() {
        return actType;
    }

    public JobInstanceAct setActType(String actType) {
        this.actType = actType;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public JobInstanceAct setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public JobInstanceAct setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public JobInstanceAct setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public JobInstanceAct setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getLogKey() {
        return logKey;
    }

    public JobInstanceAct setLogKey(String logKey) {
        this.logKey = logKey;
        return this;
    }

    public String getLogText() {
        return logText;
    }

    public JobInstanceAct setLogText(String logText) {
        this.logText = logText;
        return this;
    }

    public String getBizUri() {
        return bizUri;
    }

    public JobInstanceAct setBizUri(String bizUri) {
        this.bizUri = bizUri;
        return this;
    }
}
