package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.dispatch.trigger.sub.DailyTimeIntervalTriggerWrapper;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.invoker.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DateBuilder;
import org.quartz.Trigger;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.List;
import java.util.Set;

public class DailyTimeIntervalTriggerWithQuartzHandler implements TriggerWithQuartzHandler<DailyTimeIntervalTriggerWrapper> {

    @Override
    public DailyTimeIntervalTriggerWrapper getSpecifyTrigger(Trigger trigger) {
        DailyTimeIntervalTriggerWrapper triggerResult;
        if (trigger instanceof DailyTimeIntervalTriggerImpl) {
            DailyTimeIntervalTriggerImpl dailyTimeIntervalTrigger = (DailyTimeIntervalTriggerImpl) trigger;
            triggerResult = new DailyTimeIntervalTriggerWrapper()
                    .setStartTimeOfDay(TimeUtil.toTimeStr(dailyTimeIntervalTrigger.getStartTimeOfDay()))
                    .setEndTimeOfDay(TimeUtil.toTimeStr(dailyTimeIntervalTrigger.getEndTimeOfDay()))
                    .setRepeatCount(dailyTimeIntervalTrigger.getRepeatCount())
                    .setTimeUnit(dailyTimeIntervalTrigger.getRepeatIntervalUnit().name())
                    .setRepeatInterval(dailyTimeIntervalTrigger.getRepeatInterval())
                    .setDaysOfWeek(StringUtils.join(dailyTimeIntervalTrigger.getDaysOfWeek(), CommonConstants.COMMA));
        } else {
            SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) trigger;
            triggerResult = new DailyTimeIntervalTriggerWrapper()
                    .setRepeatCount(simpleTrigger.getRepeatCount())
                    .setTimeUnit(DateBuilder.IntervalUnit.MILLISECOND.name())
                    .setRepeatInterval(Math.toIntExact(simpleTrigger.getRepeatInterval()));
        }
        return triggerResult;
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildDailyIntervalTriggers(tenantId, paramOfTriggers);
    }
}
