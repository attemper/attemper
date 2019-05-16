package com.github.attemper.common.result.dispatch.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarInfo {

    protected String calendarName;

    protected String displayName;

    protected Integer type;

    protected Integer position;

    protected String tenantId;
}
