package com.sse.attemper.core.ext.trigger;

import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.core.ext.util.TimeUtils;
import com.sse.attemper.sdk.common.constant.SdkCommonConstants;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.DailyIntervalTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import com.sse.attemper.sys.holder.TenantHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.*;

public class DailyIntervalTriggerHandler implements TriggerHandler<DailyIntervalTriggerParam, DailyIntervalTriggerResult> {

    @Override
    public List<DailyIntervalTriggerResult> getTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        return ContextBeanAware.getBean(TriggerMapper.class).getDailyIntervalTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        ContextBeanAware.getBean(TriggerMapper.class).deleteDailyIntervalTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<DailyIntervalTriggerParam> paramOfTriggers) {
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(item -> {
            if (DateBuilder.IntervalUnit.MILLISECOND.name().equals(item.getTimeUnit())) {
                item.setTriggerType(Constants.TTYPE_SIMPLE);
            } else {
                item.setTriggerType(Constants.TTYPE_DAILY_TIME_INT);
            }
            Map<String, Object> map = BeanUtil.beanToMap(item);
            map.put(SdkCommonConstants.jobName, jobName);
            map.put(SdkCommonConstants.tenantId, TenantHolder.get().getId());
            mapList.add(map);
        });
        ContextBeanAware.getBean(TriggerMapper.class).saveDailyIntervalTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String jobName, List<DailyIntervalTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    DailyTimeIntervalScheduleBuilder builder = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
                            .withInterval(item.getInterval(), DateBuilder.IntervalUnit.valueOf(item.getTimeUnit()))
                            .endingDailyAt(TimeUtils.toTime(item.getEndTimeOfDay()))
                            .withRepeatCount(item.getRepeatCount());
                    TimeOfDay startTimeOfDay = TimeUtils.toTime(item.getStartTimeOfDay());
                    Set<Integer> days = toDaysOfTheWeek(item.getDaysOfWeek());
                    if (startTimeOfDay != null) {
                        builder.startingDailyAt(startTimeOfDay);
                    }
                    if (days != null) {
                        builder.onDaysOfTheWeek(days);
                    }
                    Trigger trigger = triggerBuilder(item).withSchedule(builder).build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }

    private Set<Integer> toDaysOfTheWeek(String daysOfWeek) {
        if (StringUtils.isBlank(daysOfWeek)) {
            return null;
        }
        String[] daysStr = daysOfWeek.split(",");
        Set<Integer> days = new HashSet<>(daysStr.length);
        for (String s : daysStr) {
            days.add(Integer.parseInt(s));
        }
        return days;
    }
}
