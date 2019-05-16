package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.java.sdk.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayCalendarConfigRemoveParam implements CommonParam {

    protected String calendarName;

    protected String dayName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(calendarName)) {
            return "6700";
        }
        if (DateUtil.parseDateStrToYYYYMMDD(dayName) == null){
            return "6705";
        }
        return null;
    }
}
