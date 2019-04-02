package com.sse.attemper.core.ext.trigger;

import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.core.ext.util.TimeUtils;
import com.sse.attemper.sdk.common.constant.SdkCommonConstants;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CalendarOffsetTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.sys.holder.TenantHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.quartz.CalendarOffsetScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.*;

public class CalendarOffsetTriggerHandler implements TriggerHandler<CalendarOffsetTriggerParam, CalendarOffsetTriggerResult> {

    @Override
    public List<CalendarOffsetTriggerResult> getTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        return ContextBeanAware.getBean(TriggerMapper.class).getCalendarOffsetTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        ContextBeanAware.getBean(TriggerMapper.class).deleteCalendarOffsetTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<CalendarOffsetTriggerParam> paramOfTriggers) {
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(item -> {
            item.setTriggerType(Constants.TTYPE_CAL_OFFSET);
            Map<String, Object> map = BeanUtil.beanToMap(item);
            map.put(SdkCommonConstants.jobName, jobName);
            map.put(SdkCommonConstants.tenantId, TenantHolder.get().getId());
            mapList.add(map);
        });
        ContextBeanAware.getBean(TriggerMapper.class).saveCalendarOffsetTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String jobName, List<CalendarOffsetTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    TimeOfDay startTimeOfDay = TimeUtils.toTime(item.getStartTimeOfDay());
                    CalendarOffsetScheduleBuilder scheduleBuilder = CalendarOffsetScheduleBuilder.calendarOffsetSchedule()
                            .withIntervalUnit(DateBuilder.IntervalUnit.valueOf(item.getTimeUnit()))
                            .withRepeatCount(item.getRepeatCount())
                            .withInnerOffset(item.getInnerOffset())
                            .withOuterOffset(item.getOuterOffset())
                            .reversed(item.getReversed());
                    if (startTimeOfDay != null) {
                        scheduleBuilder.startingDailyAt(startTimeOfDay);
                    }
                    Trigger trigger = triggerBuilder(item).withSchedule(scheduleBuilder)
                            .build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }

}
