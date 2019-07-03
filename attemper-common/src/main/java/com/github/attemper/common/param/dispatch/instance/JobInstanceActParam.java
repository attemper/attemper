package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobInstanceActParam implements CommonParam {

    protected String rootProcInstId;

    protected String actId;

    @Override
    public String validate() {
        return null;
    }
}
