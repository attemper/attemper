package com.github.attemper.common.param.dispatch.delay;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

@Data
public class DelayJobListParam extends PageSortParam {

    protected String id;

    protected String jobName;

    protected String displayName;
}
