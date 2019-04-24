package com.sse.attemper.common.param.dispatch.monitor;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceActParam implements CommonParam {

    protected String rootProcInstId;

    protected String actId;

    @Override
    public String validate() {
        return null;
    }
}
