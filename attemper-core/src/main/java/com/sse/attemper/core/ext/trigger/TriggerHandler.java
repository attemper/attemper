package com.sse.attemper.core.ext.trigger;


import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.sse.attemper.config.quartz.QuartzTriggerHandler;
import com.sse.attemper.sys.holder.TenantHolder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface TriggerHandler<K extends CommonTriggerParam, V extends CommonTriggerResult> extends QuartzTriggerHandler {

    List<V> getTriggers(Map<String, Object> jobNameWithTenantIdMap);

    void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap);

    default void deleteAndUnschedule(Map<String, Object> jobNameWithTenantIdMap) {
        List<V> resultOfTriggers = getTriggers(jobNameWithTenantIdMap);
        deleteTriggers(jobNameWithTenantIdMap);
        if (resultOfTriggers.size() > 0) {
            unscheduleTriggers(TenantHolder.get().getId(), resultOfTriggers.stream().map(V::getTriggerName).collect(Collectors.toList()));
        }
    }

    void saveTriggers(String jobName, List<K> paramOfTriggers);

    default void saveAndSchedule(String jobName, List<K> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        saveTriggers(jobName, paramOfTriggers);
        schedule(jobName, TenantHolder.get().getId(), paramOfTriggers);
    }
}
