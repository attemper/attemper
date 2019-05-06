package com.github.attemper.core.dao.mapper.job;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface TriggerMapper extends BaseMapper<Object> {

    List<CronTriggerResult> getCronTriggers(Map<String, Object> paramMap);

    void deleteCronTriggers(Map<String, Object> paramMap);

    void saveCronTriggers(List<Map<String, Object>> list);

    List<CalendarOffsetTriggerResult> getCalendarOffsetTriggers(Map<String, Object> paramMap);

    void deleteCalendarOffsetTriggers(Map<String, Object> paramMap);

    void saveCalendarOffsetTriggers(List<Map<String, Object>> list);

    List<DailyIntervalTriggerResult> getDailyIntervalTriggers(Map<String, Object> paramMap);

    void deleteDailyIntervalTriggers(Map<String, Object> paramMap);

    void saveDailyIntervalTriggers(List<Map<String, Object>> list);

    List<CalendarIntervalTriggerResult> getCalendarIntervalTriggers(Map<String, Object> paramMap);

    void deleteCalendarIntervalTriggers(Map<String, Object> paramMap);

    void saveCalendarIntervalTriggers(List<Map<String, Object>> list);
}
