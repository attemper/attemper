package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarConfigSaveParam implements CommonParam {

    protected String calendarName;

    protected String remark;

    @Override
    public String validate() {
        return null;
    }
}
