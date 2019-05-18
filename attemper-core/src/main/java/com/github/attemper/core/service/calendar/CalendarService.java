package com.github.attemper.core.service.calendar;

import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarConfigRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarConfigSaveParam;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.core.dao.mapper.calendar.CalendarMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService extends BaseServiceAdapter {

    @Autowired
    private CalendarMapper mapper;

    public List<CalendarInfo> list() {
        Map<String, Object> paramMap = injectTenantIdToMap(null);
        return mapper.list(paramMap);
    }

    public Void saveDay(DayCalendarConfigSaveParam param) {
        mapper.saveDay(injectTenantIdExceptAdminToMap(param));
        return null;
    }

    public Void removeDay(DayCalendarConfigRemoveParam param) {
        mapper.deleteDay(injectTenantIdExceptAdminToMap(param));
        return null;
    }

    public List<DayCalendarConfig> listDay(CalendarGetParam param) {
        return mapper.listDay(injectTenantIdExceptAdminToMap(param));
    }

}
