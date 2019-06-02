package com.github.attemper.common.result.dispatch.arg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Arg {

    protected String argName;

    protected Integer argType;

    protected String argValue;

    protected Integer genericType;

    protected String attribute;

    protected String remark;

    protected String tenantId;

}
