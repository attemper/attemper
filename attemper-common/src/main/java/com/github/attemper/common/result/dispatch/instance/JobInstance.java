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

    protected int status;

    protected int code;

    protected String msg;

    /**
     * when you retried, the parent id point to the parent id
     */
    protected String parentId;

    /**
     * when the job instance was retried, the retried is true
     */
    protected boolean retried;

    protected String schedulerUri;

    protected String executorUri;

    protected String tenantId;
}
