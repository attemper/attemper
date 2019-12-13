package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.param.dispatch.trigger.sub.CalendarOffsetTriggerWrapper;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.invoker.util.TimeUtil;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CalendarOffsetTriggerImpl;

import java.util.List;
import java.util.Set;

public class CalendarOffsetTriggerWithQuartzHandler implements TriggerWithQuartzHandler<CalendarOffsetTriggerWrapper> {
    @Override
    public CalendarOffsetTriggerWrapper getSpecifyTrigger(Trigger trigger) {
        CalendarOffsetTriggerImpl calendarOffsetTrigger = (CalendarOffsetTriggerImpl) trigger;
        CalendarOffsetTriggerWrapper triggerResult = new CalendarOffsetTriggerWrapper()
                .setStartTimeOfDay(TimeUtil.toTimeStr(calendarOffsetTrigger.getStartTimeOfDay()))
                .setRepeatCount(calendarOffsetTrigger.getRepeatCount())
                .setTimeUnit(calendarOffsetTrigger.getRepeatIntervalUnit().name())
                .setInnerOffset(calendarOffsetTrigger.getInnerOffset())
                .setOuterOffset(calendarOffsetTrigger.getOuterOffset())
                .setReversed(calendarOffsetTrigger.isReversed() ? 1 : 0);
        return triggerResult;
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildCalendarOffsetTriggers(tenantId, paramOfTriggers);
    }

}
