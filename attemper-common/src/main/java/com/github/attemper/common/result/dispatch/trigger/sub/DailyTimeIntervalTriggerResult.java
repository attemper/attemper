package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.Data;

@Data
public class DailyTimeIntervalTriggerResult extends CommonTriggerResult {

    protected String startTimeOfDay;

    protected String endTimeOfDay;

    protected int interval = 1;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected String daysOfWeek;

}
