package com.github.attemper.common.param.dispatch.monitor;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;

/**
 * @author ldang
 */
@Data
public class JobInstanceActParam implements CommonParam {

    protected String rootProcInstId;

    protected String actId;

    @Override
    public String validate() {
        return null;
    }
}
