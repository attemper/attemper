package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;

@ToString
public class JobInstanceActParam implements CommonParam {

    protected String rootProcInstId;

    protected String actId;

    public String getRootProcInstId() {
        return rootProcInstId;
    }

    public JobInstanceActParam setRootProcInstId(String rootProcInstId) {
        this.rootProcInstId = rootProcInstId;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public JobInstanceActParam setActId(String actId) {
        this.actId = actId;
        return this;
    }

    @Override
    public String validate() {
        return null;
    }
}
