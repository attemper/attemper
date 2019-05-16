package com.github.attemper.common.result.dispatch.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeCalendarConfig {

    protected String calendarName;

    protected String startTime;

    protected String endTime;

    protected Boolean excluded;

    protected String tenantId;
}
