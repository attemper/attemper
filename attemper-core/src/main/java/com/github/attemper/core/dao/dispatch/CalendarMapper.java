package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.param.dispatch.calendar.CalendarNameParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarListParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarRemoveParam;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CalendarMapper {

    CalendarInfo get(CalendarNameParam param);

    List<CalendarInfo> list();

    void saveDay(Map<String,Object> paramMap);

    int deleteDay(DayCalendarRemoveParam param);

    List<DayCalendarConfig> listDay(DayCalendarListParam param);

    int deleteDays(Map<String, Object> paramMap);

    void addDays(List<Map<String, Object>> paramMap);
}