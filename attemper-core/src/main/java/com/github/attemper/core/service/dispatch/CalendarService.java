package com.github.attemper.core.service.dispatch;

import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.param.dispatch.calendar.CalendarListParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarListParam;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.core.dao.dispatch.CalendarMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService extends BaseServiceAdapter {

    @Autowired
    private CalendarMapper mapper;

    public List<CalendarInfo> list(CalendarListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.list(paramMap);
    }

    public CalendarInfo get(CalendarGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public List<DayCalendarConfig> listDay(DayCalendarListParam param) {
        return mapper.listDay(injectTenantIdExceptAdminToMap(param));
    }
}
