package com.github.attemper.common.result.dispatch.instance;

import lombok.ToString;

@ToString
public class InstanceAct {

    protected String id;

    protected String actInstId;

    protected String parentActInstId;

    protected String procInstId;

    protected String rootProcInstId;

    protected String actId;

    protected String actName;

    protected String actType;

    protected Long startTime;

    protected Long endTime;

    protected Long duration;

    protected Integer status;

    protected String logKey;

    protected String logText;

    // it is not a column of xxx_instance_act
    protected String incidentMsg;

    protected String bizUri;

    public String getId() {
        return id;
    }

    public InstanceAct setId(String id) {
        this.id = id;
        return this;
    }

    public String getActInstId() {
        return actInstId;
    }

    public InstanceAct setActInstId(String actInstId) {
        this.actInstId = actInstId;
        return this;
    }

    public String getParentActInstId() {
        return parentActInstId;
    }

    public InstanceAct setParentActInstId(String parentActInstId) {
        this.parentActInstId = parentActInstId;
        return this;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public InstanceAct setProcInstId(String procInstId) {
        this.procInstId = procInstId;
        return this;
    }

    public String getRootProcInstId() {
        return rootProcInstId;
    }

    public InstanceAct setRootProcInstId(String rootProcInstId) {
        this.rootProcInstId = rootProcInstId;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public InstanceAct setActId(String actId) {
        this.actId = actId;
        return this;
    }

    public String getActName() {
        return actName;
    }

    public InstanceAct setActName(String actName) {
        this.actName = actName;
        return this;
    }

    public String getActType() {
        return actType;
    }

    public InstanceAct setActType(String actType) {
        this.actType = actType;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public InstanceAct setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public InstanceAct setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public InstanceAct setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public InstanceAct setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getLogKey() {
        return logKey;
    }

    public InstanceAct setLogKey(String logKey) {
        this.logKey = logKey;
        return this;
    }

    public String getLogText() {
        return logText;
    }

    public InstanceAct setLogText(String logText) {
        this.logText = logText;
        return this;
    }

    public String getIncidentMsg() {
        return incidentMsg;
    }

    public InstanceAct setIncidentMsg(String incidentMsg) {
        this.incidentMsg = incidentMsg;
        return this;
    }

    public String getBizUri() {
        return bizUri;
    }

    public InstanceAct setBizUri(String bizUri) {
        this.bizUri = bizUri;
        return this;
    }
}
