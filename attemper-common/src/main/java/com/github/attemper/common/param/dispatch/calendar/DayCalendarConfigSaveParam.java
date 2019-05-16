package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.java.sdk.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayCalendarConfigSaveParam extends CalendarConfigSaveParam {

    protected String dayName;

    @Override
    public String validate() {
        if (DateUtil.parseDateStrToYYYYMMDD(dayName) == null){
            return "6705";
        }
        return super.validate();
    }

}
