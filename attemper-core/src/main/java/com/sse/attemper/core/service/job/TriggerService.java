package com.sse.attemper.core.service.job;

import com.sse.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.common.param.scheduler.TriggerChangedParam;
import com.sse.attemper.common.result.dispatch.trigger.TriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.*;
import com.sse.attemper.core.ext.trigger.*;
import com.sse.attemper.core.service.SchedulerHandler;
import com.sse.attemper.sys.service.BaseServiceAdapter;
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
public class TriggerService extends BaseServiceAdapter {

    @Autowired
    private SchedulerHandler schedulerHandler;

    /**
     * you can't change the order of the array.<br>
     *
     * @see TriggerService#get(TriggerGetParam)
     */
    private static TriggerHandlerInDatabase[] triggerHandlers = new TriggerHandlerInDatabase[]{
            new CronTriggerHandler(),
            new CalendarOffsetTriggerHandler(),
            new DailyIntervalTriggerHandler(),
            new CalendarIntervalTriggerHandler()
    };

    /**
     * @param getParam
     * @return
     */
    public TriggerResult get(TriggerGetParam getParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(getParam);
        List<CronTriggerResult> cronTriggers = triggerHandlers[0].getTriggers(paramMap);
        List<CalendarOffsetTriggerResult> calendarOffsetTriggers = triggerHandlers[1].getTriggers(paramMap);
        List<DailyIntervalTriggerResult> dailyIntervalTriggers = triggerHandlers[2].getTriggers(paramMap);
        List<CalendarIntervalTriggerResult> calendarIntervalTriggers = triggerHandlers[3].getTriggers(paramMap);
        return TriggerResult.builder()
                .cronTriggers(cronTriggers)
                .calendarOffsetTriggers(calendarOffsetTriggers)
                .dailyIntervalTriggers(dailyIntervalTriggers)
                .calendarIntervalTriggers(calendarIntervalTriggers)
                .build();
    }

    /**
     * @param saveParam
     * @return
     */
    public Void update(TriggerUpdateParam saveParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(saveParam);
        Map<Integer, List<? extends CommonTriggerParam>> paramsOfTriggerMap
                = new HashMap<>();
        paramsOfTriggerMap.put(0, saveParam.getCronTriggers());
        paramsOfTriggerMap.put(1, saveParam.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, saveParam.getDailyIntervalTriggers());
        paramsOfTriggerMap.put(3, saveParam.getCalendarIntervalTriggers());
        TriggerChangedParam param = new TriggerChangedParam();
        param.setJobName(saveParam.getJobName());
        param.setOldTriggerNames(getOldTriggerNames(saveParam));
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerHandlerInDatabase triggerHandler = triggerHandlers[i];
            /*triggerHandler.getTriggers(paramMap);
            triggerHandler.deleteTriggers(paramMap);
            triggerHandler.saveTriggers(saveParam.getJobName(), paramsOfTriggerMap.get(i));*/
            triggerHandler.deleteAndUnschedule(paramMap);
            triggerHandler.saveAndSchedule(saveParam.getJobName(), paramsOfTriggerMap.get(i));
        }
        schedulerHandler.updateTrigger(param);
        return null;
    }

    public List<String> getOldTriggerNames(TriggerUpdateParam saveParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(saveParam);
        List<String> oldTriggerNames = new ArrayList<>();
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerHandlerInDatabase triggerHandler = triggerHandlers[i];
            oldTriggerNames.addAll(toList(triggerHandler.getTriggers(paramMap)));
        }
        return oldTriggerNames;
    }

    private List<String> toList(List<? extends CommonTriggerResult> oldResultOfTriggers) {
        return oldResultOfTriggers.stream().map(CommonTriggerResult::getTriggerName).collect(Collectors.toList());
    }
}
