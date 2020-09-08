package com.github.attemper.common.param.dispatch.calendar;

import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class DayCalendarListParam extends CalendarListParam {

    protected Integer lowerDayNum;

    protected Integer upperDayNum;

    @Override
    public String validate() {
        if (StringUtils.isBlank(calendarName)) {
            return "6700";
        }
        return super.validate();
    }

    public Integer getLowerDayNum() {
        return lowerDayNum;
    }

    public DayCalendarListParam setLowerDayNum(Integer lowerDayNum) {
        this.lowerDayNum = lowerDayNum;
        return this;
    }

    public Integer getUpperDayNum() {
        return upperDayNum;
    }

    public DayCalendarListParam setUpperDayNum(Integer upperDayNum) {
        this.upperDayNum = upperDayNum;
        return this;
    }
}
