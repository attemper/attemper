package com.sse.attemper.web.service.job;

import com.sse.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.param.scheduler.TriggerChangedParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.sse.attemper.core.service.job.TriggerService;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import com.sse.attemper.web.ext.trigger.*;
import com.sse.attemper.web.service.SchedulerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ldang
 */
@Service
@Transactional
public class TriggerOfSchedService extends BaseServiceAdapter {

    @Autowired
    private SchedulerHandler schedulerHandler;

    /**
     * you can't change the order of the array.<br>
     *
     * @see TriggerService#get(TriggerGetParam)
     */
    private static TriggerWithQuartzHandler[] triggerHandlers = new TriggerWithQuartzHandler[]{
            new CronTriggerWithQuartzHandler(),
            new CalendarOffsetTriggerWithQuartzHandler(),
            new DailyTimeIntervalTriggerWithQuartzHandler(),
            new CalendarIntervalTriggerWithQuartzHandler()
    };

    public Void update(TriggerUpdateParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        Map<Integer, List<? extends CommonTriggerParam>> paramsOfTriggerMap = new HashMap<>();
        paramsOfTriggerMap.put(0, param.getCronTriggers());
        paramsOfTriggerMap.put(1, param.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, param.getDailyIntervalTriggers());
        paramsOfTriggerMap.put(3, param.getCalendarIntervalTriggers());
        TriggerChangedParam triggerChangedParam = new TriggerChangedParam();
        triggerChangedParam.setJobName(param.getJobName());
        triggerChangedParam.setOldTriggerNames(getOldTriggerNames(param));
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerWithQuartzHandler triggerHandler = triggerHandlers[i];
            /*triggerHandler.getTriggers(paramMap);
            triggerHandler.deleteTriggers(paramMap);
            triggerHandler.saveTriggers(param.getJobName(), paramsOfTriggerMap.get(i));*/
            triggerHandler.deleteAndUnschedule(paramMap);
            triggerHandler.saveAndSchedule(param.getJobName(), paramsOfTriggerMap.get(i));
        }
        callScheduler(triggerChangedParam);
        return null;
    }

    /**
     * invoke scheduler app
     *
     * @param param
     */
    private void callScheduler(TriggerChangedParam param) {
        schedulerHandler.updateTrigger(param);
    }

    public List<String> getOldTriggerNames(TriggerUpdateParam saveParam) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(saveParam);
        List<String> oldTriggerNames = new ArrayList<>();
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerWithQuartzHandler triggerHandler = triggerHandlers[i];
            oldTriggerNames.addAll(toList(triggerHandler.getTriggers(paramMap)));
        }
        return oldTriggerNames;
    }

    private List<String> toList(List<? extends CommonTriggerResult> oldResultOfTriggers) {
        return oldResultOfTriggers.stream().map(CommonTriggerResult::getTriggerName).collect(Collectors.toList());
    }
}
