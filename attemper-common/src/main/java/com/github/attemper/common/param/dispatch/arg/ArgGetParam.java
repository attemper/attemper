package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;

public class ArgGetParam implements CommonParam {

    protected String argName;

    public String validate() {
        if (StringUtils.isBlank(argName)) {
            return "7000";
        }
        return null;
    }

    public String getArgName() {
        return argName;
    }

    public ArgGetParam setArgName(String argName) {
        this.argName = argName;
        return this;
    }
}
