package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class CommonTriggerResult {

    protected String triggerName;

    protected String jobName;

    protected String triggerType;

    protected Date startTime;

    protected Date endTime;

    protected List<String> calendarNames;

    protected Integer misfireInstruction;

    protected String tenantId;

}
