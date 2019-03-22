package com.sse.attemper.core.service.job;

import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.sdk.common.constant.SdkCommonConstants;
import com.sse.attemper.sdk.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CalendarIntervalTriggerParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CalendarOffsetTriggerParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CronTriggerParam;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.DailyIntervalTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.TriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.DateBuilder;
import org.quartz.impl.jdbcjobstore.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author ldang
 */
@Service
@Transactional
public class TriggerService extends BaseServiceAdapter {

    @Autowired
    private TriggerMapper mapper;

    /**
     * 根据id查询租户
     * @param getParam
     * @return
     */
    public TriggerResult get(TriggerGetParam getParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(getParam);
        List<CronTriggerResult> cronTriggers = mapper.getCronTriggers(paramMap);
        List<CalendarOffsetTriggerResult> calendarOffsetTriggers = mapper.getCalendarOffsetTriggers(paramMap);
        List<DailyIntervalTriggerResult> dailyIntervalTriggers = mapper.getDailyIntervalTriggers(paramMap);
        List<CalendarIntervalTriggerResult> calendarIntervalTriggers = mapper.getCalendarIntervalTriggers(paramMap);
        return TriggerResult.builder()
                .cronTriggers(cronTriggers)
                .calendarOffsetTriggers(calendarOffsetTriggers)
                .dailyIntervalTriggers(dailyIntervalTriggers)
                .calendarIntervalTriggers(calendarIntervalTriggers)
                .build();
    }

    /**
     * 更新
     * @param saveParam
     * @return
     */
    public Void update(TriggerUpdateParam saveParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(saveParam);
        deleteAndSaveCronTrigger(saveParam, paramMap);
        deleteAndSaveCalendarOffsetTrigger(saveParam, paramMap);
        deleteAndSaveDailyIntervalTrigger(saveParam, paramMap);
        deleteAndSaveCalendarIntervalTrigger(saveParam, paramMap);
        return null;
    }

    private void deleteAndSaveCronTrigger(TriggerUpdateParam saveParam, Map<String,Object> paramMap) {
        mapper.deleteCronTriggers(paramMap);
        List<CronTriggerParam> timeTriggers = saveParam.getCronTriggers();
        if (timeTriggers != null && timeTriggers.size() > 0) {
            List<Map<String, Object>> mapList = new ArrayList<>(timeTriggers.size());
            timeTriggers.forEach(
                    item -> {
                        item.setTriggerType(Constants.TTYPE_CRON);
                        if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                            item.setTimeZoneId(TimeZone.getDefault().getID());
                        }
                        Map<String, Object> map = BeanUtil.beanToMap(item);
                        map.put(SdkCommonConstants.jobName, saveParam.getJobName());
                        mapList.add(injectAdminedTenantIdToMap(map));
                    });
            mapper.saveCronTriggers(mapList);
        }
    }

    private void deleteAndSaveCalendarOffsetTrigger(TriggerUpdateParam saveParam, Map<String,Object> paramMap) {
        mapper.deleteCalendarOffsetTriggers(paramMap);
        List<CalendarOffsetTriggerParam> timeTriggers = saveParam.getCalendarOffsetTriggers();
        if (timeTriggers != null && timeTriggers.size() > 0) {
            List<Map<String, Object>> mapList = new ArrayList<>(timeTriggers.size());
            timeTriggers.forEach(item -> {
                item.setTriggerType(Constants.TTYPE_CAL_OFFSET);
                Map<String, Object> map = BeanUtil.beanToMap(item);
                map.put(SdkCommonConstants.jobName, saveParam.getJobName());
                mapList.add(injectAdminedTenantIdToMap(map));
            });
            mapper.saveCalendarOffsetTriggers(mapList);
        }
    }

    private void deleteAndSaveDailyIntervalTrigger(TriggerUpdateParam saveParam, Map<String,Object> paramMap) {
        mapper.deleteDailyIntervalTriggers(paramMap);
        List<DailyIntervalTriggerParam> timeTriggers = saveParam.getDailyIntervalTriggers();
        if (timeTriggers != null && timeTriggers.size() > 0) {
            List<Map<String, Object>> mapList = new ArrayList<>(timeTriggers.size());
            timeTriggers.forEach(item -> {
                if (DateBuilder.IntervalUnit.MILLISECOND.name().equals(item.getTimeUnit())) {
                    item.setTriggerType(Constants.TTYPE_SIMPLE);
                } else {
                    item.setTriggerType(Constants.TTYPE_DAILY_TIME_INT);
                }
                Map<String, Object> map = BeanUtil.beanToMap(item);
                map.put(SdkCommonConstants.jobName, saveParam.getJobName());
                mapList.add(injectAdminedTenantIdToMap(map));
            });
            mapper.saveDailyIntervalTriggers(mapList);
        }
    }

    private void deleteAndSaveCalendarIntervalTrigger(TriggerUpdateParam saveParam, Map<String,Object> paramMap) {
        mapper.deleteCalendarIntervalTriggers(paramMap);
        List<CalendarIntervalTriggerParam> timeTriggers = saveParam.getCalendarIntervalTriggers();
        if (timeTriggers != null && timeTriggers.size() > 0) {
            List<Map<String, Object>> mapList = new ArrayList<>(timeTriggers.size());
            timeTriggers.forEach(item -> {
                item.setTriggerType(Constants.TTYPE_CAL_INT);
                if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                    item.setTimeZoneId(TimeZone.getDefault().getID());
                }
                Map<String, Object> map = BeanUtil.beanToMap(item);
                map.put(SdkCommonConstants.jobName, saveParam.getJobName());
                mapList.add(injectAdminedTenantIdToMap(map));
            });
            mapper.saveCalendarIntervalTriggers(mapList);
        }
    }
}
