package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayCalendarRemoveParam implements CommonParam {

    protected String calendarName;

    protected Integer dayNum;

    @Override
    public String validate() {
        if (StringUtils.isBlank(calendarName)) {
            return "6700";
        }
        if (DateTimeUtil.parseDateStr(String.valueOf(dayNum), CommonConstants.yyyyMMdd) == null) {
            return "6705";
        }
        return null;
    }
}
