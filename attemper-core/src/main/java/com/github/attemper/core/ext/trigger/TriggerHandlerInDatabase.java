package com.github.attemper.core.ext.trigger;

import com.github.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;

import java.util.List;
import java.util.Map;

public interface TriggerHandlerInDatabase<K extends CommonTriggerParam, V extends CommonTriggerResult> {

    List<V> getTriggers(Map<String, Object> jobNameWithTenantIdMap);

}
