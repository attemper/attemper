package com.github.attemper.common.param.dispatch.trigger;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.param.dispatch.trigger.sub.*;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
public class TriggerSaveParam implements CommonParam {

    protected String jobName;

    protected List<CronTriggerParam> cronTriggers;

    protected List<CalendarOffsetTriggerParam> calendarOffsetTriggers;

    protected List<DailyIntervalTriggerParam> dailyIntervalTriggers;

    protected List<CalendarIntervalTriggerParam> calendarIntervalTriggers;

    public TriggerSaveParam() {
    }

    public TriggerSaveParam(String jobName) {
        this.jobName = jobName;
    }

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        String code = validaFieldParam(this.cronTriggers);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.calendarOffsetTriggers);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.dailyIntervalTriggers);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.calendarIntervalTriggers);
        if (code != null) {
            return code;
        }
        return null;
    }

    private String validaFieldParam(List<? extends CommonTriggerParam> list) {
        if (list != null) {
            for (CommonTriggerParam item : list) {
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
