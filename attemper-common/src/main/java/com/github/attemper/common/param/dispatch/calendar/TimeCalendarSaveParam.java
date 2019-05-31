package com.github.attemper.common.param.dispatch.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
