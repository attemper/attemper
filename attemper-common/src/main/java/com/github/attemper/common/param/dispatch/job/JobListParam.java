package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.PageSortParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobListParam extends PageSortParam {

    protected String jobName;

    protected String displayName;

    protected List<Integer> status;
}
