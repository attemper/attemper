package com.sse.attemper.common.result.dispatch.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArgAllocatedResult {

    protected String argName;

    protected Integer argType;

    protected String argValue;

    protected Boolean allocated;

}
