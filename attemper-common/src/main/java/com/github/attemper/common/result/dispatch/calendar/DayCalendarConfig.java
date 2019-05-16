package com.github.attemper.common.result.dispatch.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayCalendarConfig {

    protected String calendarName;

    protected String dayName;

    protected String remark;

    protected String tenantId;
}
