package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.PageSortParam;

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

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
