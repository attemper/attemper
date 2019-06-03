package com.github.attemper.common.param.dispatch.trigger.sub;

import lombok.Data;

@Data
public class DailyIntervalTriggerParam extends CommonTriggerParam{

    protected String startTimeOfDay;

    protected String endTimeOfDay;

    protected int interval = 1;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected String daysOfWeek;

}
