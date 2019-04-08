package com.sse.attemper.scheduler.ext.trigger;

import com.sse.attemper.common.param.dispatch.trigger.sub.CronTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.sse.attemper.config.quartz.QuartzTriggerHandler;
import com.sse.attemper.config.util.QuartzUtil;
import org.quartz.Trigger;

import java.util.List;
import java.util.Set;

public class CronTriggerHandler implements QuartzTriggerHandler<CronTriggerParam, CronTriggerResult> {

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List<CronTriggerParam> paramOfTriggers) {
        return QuartzUtil.buildCronTriggers(tenantId, paramOfTriggers);
    }
}
