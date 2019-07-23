package com.github.attemper.common.param.dispatch.instance;

public class JobInstanceGetParam {

    protected String id;

    protected String procInstId;

    public String getId() {
        return id;
    }

    public JobInstanceGetParam setId(String id) {
        this.id = id;
        return this;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public JobInstanceGetParam setProcInstId(String procInstId) {
        this.procInstId = procInstId;
        return this;
    }
}
