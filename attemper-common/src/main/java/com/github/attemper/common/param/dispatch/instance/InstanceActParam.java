package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class InstanceActParam implements CommonParam {

    protected String procInstId;

    protected String actId;

    @Override
    public String validate() {
        if (StringUtils.isBlank(procInstId)) {
            return "6200";
        }
        return null;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public InstanceActParam setProcInstId(String procInstId) {
        this.procInstId = procInstId;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public InstanceActParam setActId(String actId) {
        this.actId = actId;
        return this;
    }
}
