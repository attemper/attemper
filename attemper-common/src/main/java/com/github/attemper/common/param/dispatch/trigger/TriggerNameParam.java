package com.github.attemper.common.param.dispatch.trigger;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
