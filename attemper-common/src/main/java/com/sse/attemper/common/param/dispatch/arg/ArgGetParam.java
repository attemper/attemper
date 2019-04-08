package com.sse.attemper.common.param.dispatch.arg;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgGetParam implements CommonParam {

    protected String argName;

    public String validate() {
        if (StringUtils.isBlank(argName)) {
            return "7000";
        }
        return null;
    }

}
