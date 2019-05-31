package com.github.attemper.common.param.dispatch.calendar;

import com.github.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayCalendarListParam extends PageSortParam {

    protected String calendarName;

    protected Integer lowerDayNum;

    protected Integer upperDayNum;

    @Override
    public String validate() {
        if (StringUtils.isBlank(calendarName)) {
            return "6700";
        }
        return null;
    }
}
