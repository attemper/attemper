package com.sse.attemper.common.param.dispatch.trigger;

import com.sse.attemper.common.param.CommonParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.CalendarIntervalTriggerParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.CalendarOffsetTriggerParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.CronTriggerParam;
import com.sse.attemper.common.param.dispatch.trigger.sub.DailyIntervalTriggerParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerUpdateParam implements CommonParam {

    protected String jobName;

    protected List<CronTriggerParam> cronTriggers;

    protected List<CalendarOffsetTriggerParam> calendarOffsetTriggers;

    protected List<DailyIntervalTriggerParam> dailyIntervalTriggers;

    protected List<CalendarIntervalTriggerParam> calendarIntervalTriggers;

    public TriggerUpdateParam(String jobName) {
        this.jobName = jobName;
    }

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }

}
