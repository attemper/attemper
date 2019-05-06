package com.github.attemper.scheduler.ext.trigger;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.github.attemper.config.base.bean.ContextBeanAware;
import org.quartz.*;

import java.util.HashSet;
import java.util.List;

public class TriggerHandlerInMemory<K extends CommonTriggerParam, V extends CommonTriggerResult> {

    public void unscheduleTriggers(String tenantId, List oldTriggerNames) {
        if (oldTriggerNames != null && oldTriggerNames.isEmpty()) {
            try {
                for (Object item : oldTriggerNames) {
                    ContextBeanAware.getBean(Scheduler.class).unscheduleJobInMemory(new TriggerKey(String.valueOf(item), tenantId));
                }
            } catch (SchedulerException e) {
                throw new RTException(3002, e);
            }
        }
    }

    public void schedule(String jobName, String tenantId) {
        try {
            JobKey jobKey = new JobKey(jobName, tenantId);
            JobDetail jobDetail = ContextBeanAware.getBean(Scheduler.class).getJobDetail(jobKey);
            List<? extends Trigger> triggersOfJob = ContextBeanAware.getBean(Scheduler.class).getTriggersOfJob(jobKey);
            if (jobDetail != null && triggersOfJob != null && triggersOfJob.size() > 0) {
                ContextBeanAware.getBean(Scheduler.class).scheduleJobInMemory(jobDetail, new HashSet<>(triggersOfJob), true);
            }
        } catch (SchedulerException e) {
            throw new RTException(3003, e);
        }
    }
}
