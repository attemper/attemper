package com.sse.attemper.common.param.dispatch.arg;

import com.sse.attemper.common.enums.ArgType;
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
public class ArgSaveParam implements CommonParam {

    protected String argName;

    protected Integer argType;

    protected String defVal;

    protected String remark;

    public String validate() {
        if (StringUtils.isBlank(argName)) {
            return "7000";
        }
        if (ArgType.get(argType) == null) {
            return "7001";
        }
        return null;
    }

}
