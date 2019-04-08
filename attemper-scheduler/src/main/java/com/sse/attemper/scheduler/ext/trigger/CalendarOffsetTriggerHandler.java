package com.sse.attemper.scheduler.ext.trigger;

import com.sse.attemper.common.param.dispatch.trigger.sub.CalendarOffsetTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.config.quartz.QuartzTriggerHandler;
import com.sse.attemper.config.util.QuartzUtil;
import org.quartz.Trigger;

import java.util.List;
import java.util.Set;

public class CalendarOffsetTriggerHandler implements QuartzTriggerHandler<CalendarOffsetTriggerParam, CalendarOffsetTriggerResult> {

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List<CalendarOffsetTriggerParam> paramOfTriggers) {
        return QuartzUtil.buildCalendarOffsetTriggers(tenantId, paramOfTriggers);
    }

}
