package com.github.attemper.common.param.dispatch.trigger.sub;

import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalendarOffsetTriggerParam extends CommonTriggerParam{

    protected String startTimeOfDay;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected int innerOffset;

    protected int outerOffset;

    protected boolean reversed;

    @Override
    public String validate() {
        if (StringUtils.isBlank(timeUnit)) {
            return "6121";
        }
        return super.validate();
    }
}
