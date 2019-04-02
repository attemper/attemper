package com.sse.attemper.core.ext.trigger;


import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.ext.job.factory.JobDetailFactory;
import com.sse.attemper.sdk.common.exception.RTException;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.sse.attemper.sys.holder.TenantHolder;
import org.quartz.*;

import java.util.*;

public interface TriggerHandler<K extends CommonTriggerParam, V extends CommonTriggerResult> {

    List<V> getTriggers(Map<String, Object> jobNameWithTenantIdMap);

    void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap);

    default void unscheduleTriggers(List<V> resultOfTriggers) {
        if (resultOfTriggers.size() > 0) {
            List<TriggerKey> triggerKeys = new ArrayList<>(resultOfTriggers.size());
            resultOfTriggers.forEach(item -> {
                triggerKeys.add(new TriggerKey(item.getTriggerName(), item.getTenantId()));
            });
            try {
                ContextBeanAware.getBean(Scheduler.class).unscheduleJobs(triggerKeys);
            } catch (SchedulerException e) {
                throw new RTException(3002, e);
            }
        }
    }

    default void deleteAndUnschedule(Map<String, Object> jobNameWithTenantIdMap) {
        List<V> resultOfTriggers = getTriggers(jobNameWithTenantIdMap);
        deleteTriggers(jobNameWithTenantIdMap);
        unscheduleTriggers(resultOfTriggers);
    }

    void saveTriggers(String jobName, List<K> paramOfTriggers);

    Set<Trigger> buildTriggers(String jobName, List<K> paramOfTriggers);

    default void saveAndSchedule(String jobName, List<K> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        saveTriggers(jobName, paramOfTriggers);
        Set<Trigger> quartzTriggers = buildTriggers(jobName, paramOfTriggers);
        JobDetail jobDetail = JobDetailFactory.getJobDetail(jobName, TenantHolder.get().getId());
        try {
            ContextBeanAware.getBean(Scheduler.class).scheduleJob(jobDetail, quartzTriggers, true);
        } catch (SchedulerException e) {
            throw new RTException(3003, e);
        }
    }

    default TriggerBuilder triggerBuilder(K item) {
        return TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(item.getTriggerName(), TenantHolder.get().getId()))
                .startAt(item.getStartTime() == null ? new Date() : item.getStartTime())
                .endAt(item.getEndTime())
                .withDescription(item.getDescription());
    }
}
