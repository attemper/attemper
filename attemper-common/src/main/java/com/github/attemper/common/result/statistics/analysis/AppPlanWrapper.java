package com.github.attemper.common.result.statistics.analysis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppPlanWrapper {

    protected String jobName;

    protected Long prevFireTime;

    protected Long nextFireTime;
}
