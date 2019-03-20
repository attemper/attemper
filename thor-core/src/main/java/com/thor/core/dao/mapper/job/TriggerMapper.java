package com.thor.core.dao.mapper.job;

import com.thor.common.base.BaseMapper;
import com.thor.sdk.common.result.job.trigger.CalendarIntervalTriggerResult;
import com.thor.sdk.common.result.job.trigger.CalendarOffsetTriggerResult;
import com.thor.sdk.common.result.job.trigger.CronTriggerResult;
import com.thor.sdk.common.result.job.trigger.DailyIntervalTriggerResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
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
