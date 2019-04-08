package com.sse.attemper.scheduler.ext.trigger;

import com.sse.attemper.common.param.dispatch.trigger.sub.CalendarIntervalTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.sse.attemper.config.quartz.QuartzTriggerHandler;
import com.sse.attemper.config.util.QuartzUtil;
import org.quartz.Trigger;

import java.util.List;
import java.util.Set;

public class CalendarIntervalTriggerHandler implements QuartzTriggerHandler<CalendarIntervalTriggerParam, CalendarIntervalTriggerResult> {

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List<CalendarIntervalTriggerParam> paramOfTriggers) {
        return QuartzUtil.buildCalendarIntervalTriggers(tenantId, paramOfTriggers);
    }
}
