package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobInstanceActParam implements CommonParam {

    protected String rootProcInstId;

    protected String actId;

    @Override
    public String validate() {
        return null;
    }
}
