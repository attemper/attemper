package com.sse.attemper.scheduler.ext.trigger;

import com.sse.attemper.common.param.dispatch.trigger.sub.DailyIntervalTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import com.sse.attemper.config.quartz.QuartzTriggerHandler;
import com.sse.attemper.config.util.QuartzUtil;
import org.quartz.Trigger;

import java.util.List;
import java.util.Set;

public class DailyIntervalTriggerHandler implements QuartzTriggerHandler<DailyIntervalTriggerParam, DailyIntervalTriggerResult> {

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List<DailyIntervalTriggerParam> paramOfTriggers) {
        return QuartzUtil.buildDailyIntervalTriggers(tenantId, paramOfTriggers);
    }

}
