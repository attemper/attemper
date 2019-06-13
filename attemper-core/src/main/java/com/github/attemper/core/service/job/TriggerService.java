package com.github.attemper.core.service.job;

import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerNameParam;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.DailyTimeIntervalTriggerResult;
import com.github.attemper.core.dao.mapper.job.TriggerMapper;
import com.github.attemper.core.ext.trigger.*;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * you can't change the order of the array.<br>
     *
     * @see TriggerService#get(TriggerGetParam)
     */
    private static TriggerHandlerInDatabase[] triggerHandlers = new TriggerHandlerInDatabase[]{
            new CronTriggerHandler(),
            new CalendarOffsetTriggerHandler(),
            new DailyTimeIntervalTriggerHandler(),
            new CalendarIntervalTriggerHandler()
    };

    public TriggerResult get(TriggerGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        List<CronTriggerResult> cronTriggers = triggerHandlers[0].getTriggers(paramMap);
        List<CalendarOffsetTriggerResult> calendarOffsetTriggers = triggerHandlers[1].getTriggers(paramMap);
        List<DailyTimeIntervalTriggerResult> dailyTimeIntervalTriggers = triggerHandlers[2].getTriggers(paramMap);
        List<CalendarIntervalTriggerResult> calendarIntervalTriggers = triggerHandlers[3].getTriggers(paramMap);
        return TriggerResult.builder()
                .cronTriggers(cronTriggers)
                .calendarOffsetTriggers(calendarOffsetTriggers)
                .dailyIntervalTriggers(dailyTimeIntervalTriggers)
                .calendarIntervalTriggers(calendarIntervalTriggers)
                .build();
    }

    public List<CalendarInfo> getCalendars(TriggerNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.getCalendars(paramMap);
    }
}
