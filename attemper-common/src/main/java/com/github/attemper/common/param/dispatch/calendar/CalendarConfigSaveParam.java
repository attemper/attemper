package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalendarConfigSaveParam implements CommonParam {

    protected String calendarName;

    protected String remark;

    @Override
    public String validate() {
        return null;
    }
}
