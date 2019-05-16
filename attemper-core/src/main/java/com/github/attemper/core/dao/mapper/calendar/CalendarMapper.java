package com.github.attemper.core.dao.mapper.calendar;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CalendarMapper extends BaseMapper<CalendarInfo> {

    void saveDay(Map<String,Object> paramMap);

    int deleteDay(Map<String,Object> paramMap);

    List<DayCalendarConfig> listDay(Map<String, Object> paramMap);
}