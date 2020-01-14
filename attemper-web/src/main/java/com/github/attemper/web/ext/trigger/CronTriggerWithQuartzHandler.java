package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.param.dispatch.trigger.sub.CronTriggerWrapper;
import com.github.attemper.invoker.util.QuartzUtil;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class CronTriggerWithQuartzHandler implements TriggerWithQuartzHandler<CronTriggerWrapper> {

    @Override
    public CronTriggerWrapper getSpecifyTrigger(Trigger trigger) {
        CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
        CronTriggerWrapper triggerResult = new CronTriggerWrapper()
                .setExpression(cronTrigger.getCronExpression())
                .setTimeZoneId(cronTrigger.getTimeZone() != null ? cronTrigger.getTimeZone().getID() : TimeZone.getDefault().getID());
        return triggerResult;
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildCronTriggers(tenantId, paramOfTriggers);
    }
}
