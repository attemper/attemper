package com.github.attemper.common.param.dispatch.trigger.sub;

import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.Data;

@Data
public class CronTriggerParam extends CommonTriggerParam{

    protected String expression;

    protected String timeZoneId;

    @Override
    public String validate() {
        if (StringUtils.isBlank(expression)) {
            return "6120";
        }
        return super.validate();
    }
}
