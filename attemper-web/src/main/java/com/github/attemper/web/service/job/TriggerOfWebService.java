package com.github.attemper.web.service.job;

import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.github.attemper.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.github.attemper.common.param.scheduler.TriggerChangedParam;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.github.attemper.core.dao.mapper.job.TriggerMapper;
import com.github.attemper.core.service.job.TriggerService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.web.ext.trigger.*;
import com.github.attemper.web.service.SchedulerHandler;
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
public class TriggerOfWebService extends BaseServiceAdapter {

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

    public Void update(TriggerUpdateParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
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
            triggerHandler.deleteAndUnschedule(paramMap);
            triggerHandler.saveAndSchedule(param.getJobName(), paramsOfTriggerMap.get(i));
            List<? extends CommonTriggerParam> list = paramsOfTriggerMap.get(i);
            if (list != null) {
                for (CommonTriggerParam item : list) {
                    updateTriggerCalendar(item);
                }
            }
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
