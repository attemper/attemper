package com.sse.attemper.common.param.scheduler;

import com.sse.attemper.common.param.CommonParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerChangedParam<T extends CommonTriggerParam> implements CommonParam {

    protected String jobName;

    protected List<String> oldTriggerNames;

    protected List<CronTriggerParam> cronTriggers;

    protected List<CalendarOffsetTriggerParam> calendarOffsetTriggers;

    protected List<DailyIntervalTriggerParam> dailyIntervalTriggers;

    protected List<CalendarIntervalTriggerParam> calendarIntervalTriggers;

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }

}
