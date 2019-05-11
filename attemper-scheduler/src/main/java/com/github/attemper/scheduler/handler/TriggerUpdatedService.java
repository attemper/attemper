package com.github.attemper.scheduler.handler;

import com.github.attemper.common.param.scheduler.TriggerChangedParam;
import com.github.attemper.scheduler.ext.trigger.TriggerHandlerInMemory;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * update job or trigger in scheduler
 */
@Service
public class TriggerUpdatedService extends BaseServiceAdapter {

    private TriggerHandlerInMemory triggerHandler = new TriggerHandlerInMemory();

    public Void updateTrigger(TriggerChangedParam param) {
        String jobName = param.getJobName();
        List<String> oldTriggerNames = param.getOldTriggerNames();
        triggerHandler.unscheduleTriggers(injectTenantId(), oldTriggerNames);
        triggerHandler.schedule(jobName, injectTenantId());
        return null;
    }
}
