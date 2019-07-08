package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DayCalendarSaveParam extends CalendarConfigSaveParam {

    protected Integer dayNum;

    @Override
    public String validate() {
        if (DateTimeUtil.parseDateStr(String.valueOf(dayNum), CommonConstants.yyyyMMdd) == null) {
            return "6705";
        }
        return super.validate();
    }

}
