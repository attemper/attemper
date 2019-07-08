package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CronTriggerResult extends CommonTriggerResult {

    protected String expression;

    protected String timeZoneId;

}
