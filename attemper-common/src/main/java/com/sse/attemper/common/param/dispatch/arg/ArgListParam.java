package com.sse.attemper.common.param.dispatch.arg;

import com.sse.attemper.common.enums.ArgType;
import com.sse.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgListParam extends PageSortParam {

    protected String argName;

    protected Integer argType;

    protected String defVal;

    protected String remark;

    @Override
    public String validate() {
        if (argType != null && ArgType.get(argType) == null) {
            return "7001";
        }
        return super.validate();
    }
}
