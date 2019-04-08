package com.sse.attemper.common.param.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CronTriggerParam extends CommonTriggerParam{

    protected String expression;

    protected String timeZoneId;

}
