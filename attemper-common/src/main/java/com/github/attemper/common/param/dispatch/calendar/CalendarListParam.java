package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

@Data
public class CalendarListParam extends PageSortParam {

    protected String calendarName;

    protected Integer type;

    @Override
    public String validate() {
        return null;
    }

}
