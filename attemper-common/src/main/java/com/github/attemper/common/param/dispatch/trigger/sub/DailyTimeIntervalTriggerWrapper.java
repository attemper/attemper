package com.github.attemper.common.param.dispatch.trigger.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DailyTimeIntervalTriggerWrapper extends CommonTriggerWrapper {

    protected String startTimeOfDay;

    protected String endTimeOfDay;

    protected int repeatInterval = 1;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected String daysOfWeek;

    public String getStartTimeOfDay() {
        return startTimeOfDay;
    }

    public DailyTimeIntervalTriggerWrapper setStartTimeOfDay(String startTimeOfDay) {
        this.startTimeOfDay = startTimeOfDay;
        return this;
    }

    public String getEndTimeOfDay() {
        return endTimeOfDay;
    }

    public DailyTimeIntervalTriggerWrapper setEndTimeOfDay(String endTimeOfDay) {
        this.endTimeOfDay = endTimeOfDay;
        return this;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public DailyTimeIntervalTriggerWrapper setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
        return this;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public DailyTimeIntervalTriggerWrapper setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public DailyTimeIntervalTriggerWrapper setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public DailyTimeIntervalTriggerWrapper setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        return this;
    }
}
