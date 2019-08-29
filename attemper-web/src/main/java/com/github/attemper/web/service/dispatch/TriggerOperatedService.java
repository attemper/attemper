package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerSaveParam;
import com.github.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.github.attemper.common.param.scheduler.TriggerChangedParam;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.github.attemper.core.dao.dispatch.TriggerMapper;
import com.github.attemper.core.service.dispatch.TriggerService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.web.ext.app.SchedulerHandler;
import com.github.attemper.web.ext.trigger.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TriggerOperatedService extends BaseServiceAdapter {

    @Autowired
    private TriggerMapper mapper;

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

    public Void update(TriggerSaveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        Map<Integer, List<? extends CommonTriggerParam>> paramsOfTriggerMap = new HashMap<>(4);
        paramsOfTriggerMap.put(0, param.getCronTriggers());
        paramsOfTriggerMap.put(1, param.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, param.getDailyIntervalTriggers());
        paramsOfTriggerMap.put(3, param.getCalendarIntervalTriggers());
        TriggerChangedParam triggerChangedParam = new TriggerChangedParam();
        triggerChangedParam.setJobName(param.getJobName());
        triggerChangedParam.setOldTriggerNames(getOldTriggerNames(param));
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerWithQuartzHandler triggerHandler = triggerHandlers[i];
            triggerHandler.deleteAndUnschedule(paramMap);
            triggerHandler.saveAndSchedule(param.getJobName(), param.getJobDataMap(), paramsOfTriggerMap.get(i));
            List<? extends CommonTriggerParam> list = paramsOfTriggerMap.get(i);
            if (list != null) {
                for (CommonTriggerParam item : list) {
                    updateTriggerCalendar(item);
                }
            }
        }
        schedulerHandler.updateTrigger(triggerChangedParam);
        return null;
    }

    public List<String> getOldTriggerNames(TriggerSaveParam saveParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(saveParam);
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

    public int updateTriggerCalendar(CommonTriggerParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        int deletedRows = mapper.deleteTriggerCalendars(paramMap);
        if (param.getCalendarNames() != null && !param.getCalendarNames().isEmpty()) {
            mapper.saveTriggerCalendars(paramMap);
        }
        return deletedRows;
    }
}
