package com.github.attemper.common.result.dispatch.trigger;

import com.github.attemper.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.DailyTimeIntervalTriggerResult;
import lombok.*;

import java.util.List;

@ToString
public class TriggerResult {

    protected List<CronTriggerResult> cronTriggers;

    protected List<CalendarOffsetTriggerResult> calendarOffsetTriggers;

    protected List<DailyTimeIntervalTriggerResult> dailyIntervalTriggers;

    protected List<CalendarIntervalTriggerResult> calendarIntervalTriggers;

    public List<CronTriggerResult> getCronTriggers() {
        return cronTriggers;
    }

    public TriggerResult setCronTriggers(List<CronTriggerResult> cronTriggers) {
        this.cronTriggers = cronTriggers;
        return this;
    }

    public List<CalendarOffsetTriggerResult> getCalendarOffsetTriggers() {
        return calendarOffsetTriggers;
    }

    public TriggerResult setCalendarOffsetTriggers(List<CalendarOffsetTriggerResult> calendarOffsetTriggers) {
        this.calendarOffsetTriggers = calendarOffsetTriggers;
        return this;
    }

    public List<DailyTimeIntervalTriggerResult> getDailyIntervalTriggers() {
        return dailyIntervalTriggers;
    }

    public TriggerResult setDailyIntervalTriggers(List<DailyTimeIntervalTriggerResult> dailyIntervalTriggers) {
        this.dailyIntervalTriggers = dailyIntervalTriggers;
        return this;
    }

    public List<CalendarIntervalTriggerResult> getCalendarIntervalTriggers() {
        return calendarIntervalTriggers;
    }

    public TriggerResult setCalendarIntervalTriggers(List<CalendarIntervalTriggerResult> calendarIntervalTriggers) {
        this.calendarIntervalTriggers = calendarIntervalTriggers;
        return this;
    }
}
