package com.sse.attemper.common.param.dispatch.arg;

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

    protected String argValue;

    protected String remark;
}
