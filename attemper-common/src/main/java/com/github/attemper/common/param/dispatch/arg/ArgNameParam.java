package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class ArgNameParam implements CommonParam {

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

    public ArgNameParam setArgName(String argName) {
        this.argName = argName;
        return this;
    }
}
