package com.github.attemper.core.service.dispatch;

import com.github.attemper.common.param.dispatch.calendar.CalendarNameParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarListParam;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.core.dao.dispatch.CalendarMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService extends BaseServiceAdapter {

    @Autowired
    private CalendarMapper mapper;

    public List<CalendarInfo> list() {
        return mapper.list();
    }

    public CalendarInfo get(CalendarNameParam param) {
        return mapper.get(param);
    }

    public List<DayCalendarConfig> listDay(DayCalendarListParam param) {
        return mapper.listDay(param);
    }
}
