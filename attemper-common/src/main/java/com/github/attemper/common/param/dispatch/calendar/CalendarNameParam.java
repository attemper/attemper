package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class CalendarNameParam implements CommonParam {

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

    public CalendarNameParam setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }
}
