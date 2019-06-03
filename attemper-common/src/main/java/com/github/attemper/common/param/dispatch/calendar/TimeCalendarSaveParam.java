package com.github.attemper.common.param.dispatch.calendar;

import lombok.Data;

@Data
public class TimeCalendarSaveParam extends CalendarConfigSaveParam {

    protected String startTime;

    protected String endTime;

    protected Boolean excluded;

    @Override
    public String validate() {
        // TODO
        return super.validate();
    }
}
