package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.Data;

@Data
public class CalendarIntervalTriggerResult extends CommonTriggerResult {

    protected int interval = 1;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected boolean preserveDayLight;

    protected boolean skipDayIfNoHour;

    protected String timeZoneId;

}
