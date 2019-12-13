package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.param.dispatch.trigger.sub.CalendarIntervalTriggerWrapper;
import com.github.attemper.invoker.util.QuartzUtil;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;

import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class CalendarIntervalTriggerWithQuartzHandler implements TriggerWithQuartzHandler<CalendarIntervalTriggerWrapper> {

    @Override
    public CalendarIntervalTriggerWrapper getSpecifyTrigger(Trigger trigger) {
        CalendarIntervalTriggerImpl calendarIntervalTrigger = (CalendarIntervalTriggerImpl) trigger;
        CalendarIntervalTriggerWrapper triggerResult = new CalendarIntervalTriggerWrapper()
                .setRepeatCount(calendarIntervalTrigger.getRepeatCount())
                .setTimeUnit(calendarIntervalTrigger.getRepeatIntervalUnit().name())
                .setRepeatInterval(calendarIntervalTrigger.getRepeatInterval())
                .setTimeZoneId(calendarIntervalTrigger.getTimeZone() != null ? calendarIntervalTrigger.getTimeZone().getID() : TimeZone.getDefault().getID())
                .setPreserveDayLight(calendarIntervalTrigger.isPreserveHourOfDayAcrossDaylightSavings() ? 1 : 0)
                .setSkipDayIfNoHour(calendarIntervalTrigger.isSkipDayIfHourDoesNotExist() ? 1 : 0);
        return triggerResult;
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildCalendarIntervalTriggers(tenantId, paramOfTriggers);
    }
}
