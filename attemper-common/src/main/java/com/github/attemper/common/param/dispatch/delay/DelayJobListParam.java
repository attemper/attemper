package com.github.attemper.common.param.dispatch.delay;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelayJobListParam extends PageSortParam {

    protected String id;

    protected String jobName;

    protected String displayName;
}
