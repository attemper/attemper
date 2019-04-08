package com.sse.attemper.config.quartz;

import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.util.QuartzUtil;
import org.quartz.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface QuartzTriggerHandler<K extends CommonTriggerParam, V extends CommonTriggerResult> {

    default void unscheduleTriggers(String tenantId, List<String> oldTriggerNames) {
        if (oldTriggerNames.size() > 0) {
            List<TriggerKey> triggerKeys = new ArrayList<>(oldTriggerNames.size());
            oldTriggerNames.forEach(item -> {
                try {
                    ContextBeanAware.getBean(Scheduler.class).unscheduleJob(new TriggerKey(item, tenantId));
                } catch (SchedulerException e) {
                    throw new RTException(3002, e);
                }
            });
        }
    }

    Set<Trigger> buildTriggers(String tenantId, List<K> paramOfTriggers);

    default void schedule(String jobName, String tenantId, List<K> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        Set<Trigger> quartzTriggers = buildTriggers(tenantId, paramOfTriggers);
        JobDetail jobDetail = QuartzUtil.newJobDetail(jobName, tenantId);
        try {
            ContextBeanAware.getBean(Scheduler.class).scheduleJob(jobDetail, quartzTriggers, true);
        } catch (SchedulerException e) {
            throw new RTException(3003, e);
        }
    }

    default TriggerBuilder triggerBuilder(String tenantId, K item) {
        return QuartzUtil.triggerBuilder(tenantId, item);
    }
}