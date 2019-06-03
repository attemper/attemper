package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

@Data
public class JobArgAllocatedParam extends PageSortParam {

    protected String jobName;

    protected String argName;
}
