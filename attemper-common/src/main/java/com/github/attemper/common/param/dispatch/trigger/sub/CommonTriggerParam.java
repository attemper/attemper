package com.github.attemper.common.param.dispatch.trigger.sub;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public abstract class CommonTriggerParam implements CommonParam {

    protected String triggerName;

    protected String triggerType;

    protected String description;

    protected Date startTime;

    protected Date endTime;

    protected List<String> calendarNames;

    @Override
    public String validate() {
        if (StringUtils.isBlank(triggerName)) {
            return "6100";
        }
        if (endTime != null) {
            if (!startTime.before(endTime)) {
                return "6110";
            } else if (!endTime.after(new Date())) {
                return "6111";
            }
        }
        return null;
    }

    @Override
    public void preHandle() {
        Date now = new Date();
        if (startTime == null) {
            startTime = new Date(now.getTime() + 3000L);
        }
    }
}
