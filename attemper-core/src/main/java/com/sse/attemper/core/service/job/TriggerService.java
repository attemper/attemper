package com.sse.attemper.core.service.job;

import com.sse.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.common.result.dispatch.trigger.TriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.sse.attemper.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import com.sse.attemper.core.ext.trigger.*;
import com.sse.attemper.sys.service.BaseServiceAdapter;
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
}
