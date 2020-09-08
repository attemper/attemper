package com.github.attemper.common.param.dispatch.trigger.sub;

import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalendarOffsetTriggerWrapper extends CommonTriggerWrapper {

    protected String startTimeOfDay;

    protected String timeUnit;

    protected int repeatCount = -1;

    protected int innerOffset;

    protected int outerOffset;

    protected int reversed;

    @Override
    public String validate() {
        if (StringUtils.isBlank(timeUnit)) {
            return "6121";
        }
        return super.validate();
    }

    public String getStartTimeOfDay() {
        return startTimeOfDay;
    }

    public CalendarOffsetTriggerWrapper setStartTimeOfDay(String startTimeOfDay) {
        this.startTimeOfDay = startTimeOfDay;
        return this;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public CalendarOffsetTriggerWrapper setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public CalendarOffsetTriggerWrapper setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public int getInnerOffset() {
        return innerOffset;
    }

    public CalendarOffsetTriggerWrapper setInnerOffset(int innerOffset) {
        this.innerOffset = innerOffset;
        return this;
    }

    public int getOuterOffset() {
        return outerOffset;
    }

    public CalendarOffsetTriggerWrapper setOuterOffset(int outerOffset) {
        this.outerOffset = outerOffset;
        return this;
    }

    public int getReversed() {
        return reversed;
    }

    public CalendarOffsetTriggerWrapper setReversed(int reversed) {
        this.reversed = reversed;
        return this;
    }
}
