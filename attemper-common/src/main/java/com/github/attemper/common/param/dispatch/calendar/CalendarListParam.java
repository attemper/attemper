package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.PageSortParam;
import lombok.ToString;

@ToString
public class CalendarListParam extends PageSortParam {

    protected String calendarName;

    public String getCalendarName() {
        return calendarName;
    }

    public CalendarListParam setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }
}
