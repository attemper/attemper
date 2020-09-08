package com.github.attemper.common.param.dispatch.trigger.sub;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public abstract class CommonTriggerWrapper implements CommonParam {

    protected String triggerName;

    protected String triggerType;

    protected Long startTime;

    protected Long endTime;

    protected Integer misfireInstruction;

    protected List<String> calendarNames;

    @Override
    public String validate() {
        if (StringUtils.isBlank(triggerName)) {
            return "6100";
        }
        if (startTime != null && endTime != null) {
            if (startTime > endTime) {
                return "6110";
            } else if (endTime < System.currentTimeMillis()) {
                return "6111";
            }
        }
        return null;
    }

    @Override
    public void preHandle() {
        if (misfireInstruction == null) {
            misfireInstruction = 2;
        }
        if (calendarNames != null && calendarNames.size() > 0) {
            Collections.sort(calendarNames);
        }
    }
}
