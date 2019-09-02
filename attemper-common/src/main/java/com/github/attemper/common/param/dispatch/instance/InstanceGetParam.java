package com.github.attemper.common.param.dispatch.instance;

public class InstanceGetParam {

    protected String id;

    protected String procInstId;

    public String getId() {
        return id;
    }

    public InstanceGetParam setId(String id) {
        this.id = id;
        return this;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public InstanceGetParam setProcInstId(String procInstId) {
        this.procInstId = procInstId;
        return this;
    }
}
