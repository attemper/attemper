package com.github.attemper.common.param.dispatch.trigger;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerNameParam implements CommonParam {

    protected String triggerName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(triggerName)) {
            return "6100";
        }
        return null;
    }
}
