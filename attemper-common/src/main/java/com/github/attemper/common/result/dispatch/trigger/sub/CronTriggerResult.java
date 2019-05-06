package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronTriggerResult extends CommonTriggerResult {

    protected String expression;

    protected String timeZoneId;

}
