package com.sse.attemper.common.param.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarIntervalTriggerParam extends CommonTriggerParam{

    protected int interval = 1;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected boolean preserveDayLight;

    protected boolean skipDayIfNoHour;

    protected String timeZoneId;

}
