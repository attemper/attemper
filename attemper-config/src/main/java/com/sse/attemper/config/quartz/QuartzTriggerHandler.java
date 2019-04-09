package com.sse.attemper.config.quartz;

import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;

import java.util.List;

public interface QuartzTriggerHandler<K extends CommonTriggerParam, V extends CommonTriggerResult> {

    void unscheduleTriggers(String tenantId, List<String> oldTriggerNames);
}