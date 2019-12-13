package com.github.attemper.common.param.dispatch.trigger.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalendarIntervalTriggerWrapper extends CommonTriggerWrapper {

    protected int repeatInterval = 1;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected int preserveDayLight;

    protected int skipDayIfNoHour;

    protected String timeZoneId;

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public CalendarIntervalTriggerWrapper setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
        return this;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public CalendarIntervalTriggerWrapper setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public CalendarIntervalTriggerWrapper setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public int getPreserveDayLight() {
        return preserveDayLight;
    }

    public CalendarIntervalTriggerWrapper setPreserveDayLight(int preserveDayLight) {
        this.preserveDayLight = preserveDayLight;
        return this;
    }

    public int getSkipDayIfNoHour() {
        return skipDayIfNoHour;
    }

    public CalendarIntervalTriggerWrapper setSkipDayIfNoHour(int skipDayIfNoHour) {
        this.skipDayIfNoHour = skipDayIfNoHour;
        return this;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public CalendarIntervalTriggerWrapper setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
        return this;
    }
}
