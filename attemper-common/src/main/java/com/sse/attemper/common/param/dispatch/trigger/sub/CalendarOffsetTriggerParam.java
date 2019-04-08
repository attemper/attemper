package com.sse.attemper.common.param.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarOffsetTriggerParam extends CommonTriggerParam{

    protected String startTimeOfDay;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected int innerOffset;

    protected int outerOffset;

    protected boolean reversed;

}
