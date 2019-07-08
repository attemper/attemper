package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.PageSortParam;
import lombok.ToString;

@ToString
public class CalendarListParam extends PageSortParam {

    protected String calendarName;

    protected Integer type;

    @Override
    public String validate() {
        return null;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public CalendarListParam setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public CalendarListParam setType(Integer type) {
        this.type = type;
        return this;
    }
}
