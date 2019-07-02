package com.github.attemper.common.result.dispatch.delay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelayJob {

    protected String id;

    protected String jobName;

    protected String displayName;

    protected Integer status;

    protected List<Date> nextFireTimes;

    protected Date requestTime;

    protected String tenantId;
}
