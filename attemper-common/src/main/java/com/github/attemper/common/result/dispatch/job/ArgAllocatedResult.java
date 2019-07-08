package com.github.attemper.common.result.dispatch.job;

import lombok.*;

@Getter
@Setter
@ToString
public class ArgAllocatedResult {

    protected String argName;

    protected Integer argType;

    protected String argValue;

    protected Boolean allocated;

}
