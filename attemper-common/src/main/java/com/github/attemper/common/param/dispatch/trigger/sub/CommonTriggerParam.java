package com.github.attemper.common.param.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class CommonTriggerParam {

    protected String triggerName;

    protected String triggerType;

    protected String description;

    protected Date startTime;

    protected Date endTime;

}
