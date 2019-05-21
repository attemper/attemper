package com.github.attemper.common.param.dispatch.trigger.sub;

import com.github.attemper.java.sdk.common.util.StringUtils;
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

    @Override
    public String validate() {
        if (StringUtils.isBlank(expression)) {
            return "6120";
        }
        return super.validate();
    }
}
