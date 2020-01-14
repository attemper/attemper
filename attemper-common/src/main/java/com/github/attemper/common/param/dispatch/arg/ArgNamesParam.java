package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;

import java.util.List;

@ToString
public class ArgNamesParam implements CommonParam {

    protected List<String> argNames;

    public String validate() {
        if (argNames == null || argNames.isEmpty()) {
            return "7000";
        }
        return null;
    }

    public List<String> getArgNames() {
        return argNames;
    }

    public ArgNamesParam setArgNames(List<String> argNames) {
        this.argNames = argNames;
        return this;
    }
}
