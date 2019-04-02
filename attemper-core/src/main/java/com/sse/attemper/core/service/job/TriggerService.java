package com.sse.attemper.core.service.job;

import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.core.ext.trigger.*;
import com.sse.attemper.sdk.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CommonTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.TriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class TriggerService extends BaseServiceAdapter {

    @Autowired
    private TriggerMapper mapper;

    @Autowired
    private Scheduler scheduler;

    /**
     * you can't change the order of the array.<br>
     *
     * @see TriggerService#get(TriggerGetParam)
     */
    private static TriggerHandler[] triggerHandlers = new TriggerHandler[]{
            new CronTriggerHandler(),
            new CalendarOffsetTriggerHandler(),
            new DailyIntervalTriggerHandler(),
            new CalendarIntervalTriggerHandler()
    };

    /**
     * 根据id查询租户
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
     * 更新
     * @param saveParam
     * @return
     */
    public Void update(TriggerUpdateParam saveParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(saveParam);
        Map<Integer, List<? extends CommonTriggerParam>> paramsOfTriggerMap
                = new HashMap<Integer, List<? extends CommonTriggerParam>>();
        paramsOfTriggerMap.put(0, saveParam.getCronTriggers());
        paramsOfTriggerMap.put(1, saveParam.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, saveParam.getDailyIntervalTriggers());
        paramsOfTriggerMap.put(3, saveParam.getCalendarIntervalTriggers());
        for (int i = 0; i < triggerHandlers.length; i++) {
            TriggerHandler triggerHandler = triggerHandlers[i];
            triggerHandler.deleteAndUnschedule(paramMap);
            triggerHandler.saveAndSchedule(saveParam.getJobName(), paramsOfTriggerMap.get(i));
        }
        return null;
    }
}
