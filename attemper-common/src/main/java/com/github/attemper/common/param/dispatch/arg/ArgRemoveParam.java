package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;

import java.util.List;

@Data
public class ArgRemoveParam implements CommonParam {

    protected List<String> argNames;

    public String validate() {
        if (argNames == null || argNames.isEmpty()) {
            return "7000";
        }
        return null;
    }
}
