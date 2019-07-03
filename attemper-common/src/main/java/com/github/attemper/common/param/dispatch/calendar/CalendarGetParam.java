package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;

public class CalendarGetParam implements CommonParam {

    protected String calendarName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(calendarName)) {
            return "6700";
        }
        return null;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public CalendarGetParam setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }
}
