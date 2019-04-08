package com.sse.attemper.common.result.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarOffsetTriggerResult extends CommonTriggerResult {

    protected String startTimeOfDay;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected int innerOffset;

    protected int outerOffset;

    protected boolean reversed;

}
