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

    /**
     * @param saveParam
     * @return
     */
    public Void update(TriggerUpdateParam saveParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(saveParam);
        Map<Integer, List<? extends CommonTriggerParam>> paramsOfTriggerMap = new HashMap<>();
        paramsOfTriggerMap.put(0, saveParam.getCronTriggers());
        paramsOfTriggerMap.put(1, saveParam.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, saveParam.getDailyIntervalTriggers());
        paramsOfTriggerMap.put(3, saveParam.getCalendarIntervalTriggers());
        TriggerChangedParam param = new TriggerChangedParam();
        param.setJobName(saveParam.getJobName());
        param.setOldTriggerNames(getOldTriggerNames(saveParam));
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerWithQuartzHandler triggerHandler = triggerHandlers[i];
            /*triggerHandler.getTriggers(paramMap);
            triggerHandler.deleteTriggers(paramMap);
            triggerHandler.saveTriggers(saveParam.getJobName(), paramsOfTriggerMap.get(i));*/
            triggerHandler.deleteAndUnschedule(paramMap);
            triggerHandler.saveAndSchedule(saveParam.getJobName(), paramsOfTriggerMap.get(i));
        }
        callScheduler(param);
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
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(saveParam);
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
