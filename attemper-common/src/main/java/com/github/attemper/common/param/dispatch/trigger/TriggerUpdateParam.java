package com.github.attemper.common.param.dispatch.trigger;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.param.dispatch.trigger.sub.*;
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
