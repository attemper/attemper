package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

import java.util.List;

/**
 * @author ldang
 */
@Data
public class JobListParam extends PageSortParam {

    protected String jobName;

    protected String displayName;

    protected List<Integer> status;
}
