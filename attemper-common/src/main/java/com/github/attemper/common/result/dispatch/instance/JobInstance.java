package com.github.attemper.common.result.dispatch.instance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInstance {

    protected String id;

    protected String procInstId;

    protected String rootProcInstId;

    protected String jobName;

    protected String displayName;

    protected String triggerName;

    protected String procDefId;

    protected Date startTime;

    protected Date endTime;

    protected Long duration;

    protected Integer status;

    protected Integer code;

    protected String msg;

    protected String tenantId;

    protected String schedulerUri;

    protected String executorUri;
}
