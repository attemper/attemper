package com.sse.attemper.web.ext.trigger;


import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.scheduler.util.QuartzUtil;
import com.sse.attemper.core.ext.trigger.TriggerHandlerInDatabase;
import com.sse.attemper.sys.holder.TenantHolder;
import org.quartz.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface TriggerWithQuartzHandler<K extends CommonTriggerParam, V extends CommonTriggerResult> extends TriggerHandlerInDatabase {

    void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap);

    default void unscheduleTriggers(String tenantId, List oldTriggerNames) {
        if (oldTriggerNames.size() > 0) {
            oldTriggerNames.forEach(item -> {
                try {
                    ContextBeanAware.getBean(Scheduler.class).unscheduleJob(new TriggerKey(String.valueOf(item), tenantId));
                } catch (SchedulerException e) {
                    throw new RTException(3002, e);
                }
            });
        }
    }

    default void deleteAndUnschedule(Map<String, Object> jobNameWithTenantIdMap) {
        List<V> resultOfTriggers = getTriggers(jobNameWithTenantIdMap);
        deleteTriggers(jobNameWithTenantIdMap);
        if (resultOfTriggers.size() > 0) {
            unscheduleTriggers(TenantHolder.get().getId(), resultOfTriggers.stream().map(V::getTriggerName).collect(Collectors.toList()));
        }
    }

    void saveTriggers(String jobName, List<K> paramOfTriggers);

    Set<Trigger> buildTriggers(String tenantId, List<K> paramOfTriggers);

    default void schedule(String jobName, String tenantId, List paramOfTriggers) {
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

    default void saveAndSchedule(String jobName, List<K> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        saveTriggers(jobName, paramOfTriggers);
        schedule(jobName, TenantHolder.get().getId(), paramOfTriggers);
    }
}
