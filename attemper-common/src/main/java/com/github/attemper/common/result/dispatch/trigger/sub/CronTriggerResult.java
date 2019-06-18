package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.Data;

@Data
public class CronTriggerResult extends CommonTriggerResult {

    protected String expression;

    protected String timeZoneId;

}
