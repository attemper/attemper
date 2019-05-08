package com.github.attemper.common.result.dispatch.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceAct {

    protected String id;

    protected String actInstId;

    protected String parentActInstId;

    protected String executionId;

    protected String procInstId;

    protected String rootProcInstId;

    protected String actId;

    protected String actName;

    protected String actType;

    protected Date startTime;

    protected Date endTime;

    protected Long duration;

    protected Integer status;

    protected String logKey;

    protected String logText;

}
