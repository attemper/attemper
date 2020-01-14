package com.github.attemper.common.param.dispatch.trigger.sub;

import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.ToString;

@ToString
public class CronTriggerWrapper extends CommonTriggerWrapper {

    protected String expression;

    protected String timeZoneId;

    @Override
    public String validate() {
        if (StringUtils.isBlank(expression)) {
            return "6120";
        }
        return super.validate();
    }

    public String getExpression() {
        return expression;
    }

    public CronTriggerWrapper setExpression(String expression) {
        this.expression = expression;
        return this;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public CronTriggerWrapper setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
        return this;
    }
}
