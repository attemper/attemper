package com.sse.attemper.common.result.dispatch.trigger;

import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerResult {

    protected List<CronTriggerResult> cronTriggers;

    protected List<CalendarOffsetTriggerResult> calendarOffsetTriggers;

    protected List<DailyIntervalTriggerResult> dailyIntervalTriggers;

    protected List<CalendarIntervalTriggerResult> calendarIntervalTriggers;

}
