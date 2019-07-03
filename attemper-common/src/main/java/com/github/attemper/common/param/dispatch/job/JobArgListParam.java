package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobArgListParam extends PageSortParam {

    protected String jobName;

    protected String argName;

    protected Integer argType;

    protected String argValue;
}
