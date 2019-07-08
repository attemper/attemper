package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ArgRemoveParam implements CommonParam {

    protected List<String> argNames;

    public String validate() {
        if (argNames == null || argNames.isEmpty()) {
            return "7000";
        }
        return null;
    }
}
