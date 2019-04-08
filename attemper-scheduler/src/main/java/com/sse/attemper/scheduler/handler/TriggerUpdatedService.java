package com.sse.attemper.scheduler.handler;

import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.param.scheduler.TriggerChangedParam;
import com.sse.attemper.config.quartz.QuartzTriggerHandler;
import com.sse.attemper.scheduler.ext.trigger.CalendarIntervalTriggerHandler;
import com.sse.attemper.scheduler.ext.trigger.CalendarOffsetTriggerHandler;
import com.sse.attemper.scheduler.ext.trigger.CronTriggerHandler;
import com.sse.attemper.scheduler.ext.trigger.DailyIntervalTriggerHandler;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * update job or trigger in scheduler
 */
@Service
public class TriggerUpdatedService extends BaseServiceAdapter {

    @Autowired
    private Scheduler scheduler;

    private static QuartzTriggerHandler[] triggerHandlers = new QuartzTriggerHandler[]{
            new CronTriggerHandler(),
            new CalendarOffsetTriggerHandler(),
            new DailyIntervalTriggerHandler(),
            new CalendarIntervalTriggerHandler()
    };

    public Void updateTrigger(TriggerChangedParam param) {
        Map<Integer, List<? extends CommonTriggerParam>> paramsOfTriggerMap = new HashMap<>();
        paramsOfTriggerMap.put(0, param.getCronTriggers());
        paramsOfTriggerMap.put(1, param.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, param.getDailyIntervalTriggers());
        paramsOfTriggerMap.put(3, param.getCalendarIntervalTriggers());
        String jobName = param.getJobName();
        String tenantId = injectAdminedTenant().getId();
        triggerHandlers[0].unscheduleTriggers(tenantId, param.getOldTriggerNames());
        for (int i = 0; i < triggerHandlers.length; i++) {
            QuartzTriggerHandler triggerHandler = triggerHandlers[i];
            triggerHandler.schedule(jobName, injectAdminedTenant().getId(), paramsOfTriggerMap.get(i));
        }

        return null;
    }
}
