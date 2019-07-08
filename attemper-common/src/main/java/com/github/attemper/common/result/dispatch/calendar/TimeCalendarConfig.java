package com.github.attemper.common.result.dispatch.calendar;

import lombok.*;

@Getter
@Setter
@ToString
public class TimeCalendarConfig {

    protected String calendarName;

    protected String startTime;

    protected String endTime;

    protected Boolean excluded;

    protected String tenantId;
}
