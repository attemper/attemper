package com.github.attemper.common.param.dispatch.trigger;

import com.github.attemper.common.param.dispatch.job.JobNameParam;
import com.github.attemper.common.param.dispatch.trigger.sub.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class TriggerWrapper extends JobNameParam {

    protected Map<String, Object> jobDataMap;

    protected List<CronTriggerWrapper> cronTriggers;

    protected List<CalendarOffsetTriggerWrapper> calendarOffsetTriggers;

    protected List<DailyTimeIntervalTriggerWrapper> dailyTimeIntervalTriggers;

    protected List<CalendarIntervalTriggerWrapper> calendarIntervalTriggers;

    public TriggerWrapper() {
    }

    public TriggerWrapper(String jobName) {
        this.jobName = jobName;
    }

    public String validate() {
        String code = validaFieldParam(this.cronTriggers);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.calendarOffsetTriggers);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.dailyTimeIntervalTriggers);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.calendarIntervalTriggers);
        if (code != null) {
            return code;
        }
        return super.validate();
    }

    private String validaFieldParam(List<? extends CommonTriggerWrapper> list) {
        if (list != null) {
            for (CommonTriggerWrapper item : list) {
                item.preHandle();
                String code = item.validate();
                if (item.validate() != null) {
                    return code;
                }
            }
        }
        return null;
    }
}
