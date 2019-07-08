package com.github.attemper.common.result.dispatch.calendar;

import lombok.*;

@Getter
@Setter
@ToString
public class CalendarInfo {

    protected String calendarName;

    protected String displayName;

    protected Integer type;

    protected Integer position;

    protected String tenantId;
}
