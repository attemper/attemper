package com.sse.attemper.web.ext.trigger;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.param.dispatch.trigger.sub.DailyIntervalTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.DailyIntervalTriggerResult;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.scheduler.util.QuartzUtil;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.core.ext.trigger.DailyTimeIntervalTriggerHandler;
import com.sse.attemper.sys.holder.TenantHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.quartz.DateBuilder;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DailyTimeIntervalTriggerWithQuartzHandler extends DailyTimeIntervalTriggerHandler implements TriggerWithQuartzHandler<DailyIntervalTriggerParam, DailyIntervalTriggerResult> {

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        ContextBeanAware.getBean(TriggerMapper.class).deleteDailyIntervalTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<DailyIntervalTriggerParam> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(item -> {
            if (DateBuilder.IntervalUnit.MILLISECOND.name().equals(item.getTimeUnit())) {
                item.setTriggerType(Constants.TTYPE_SIMPLE);
            } else {
                item.setTriggerType(Constants.TTYPE_DAILY_TIME_INT);
            }
            Map<String, Object> map = BeanUtil.beanToMap(item);
            map.put(CommonConstants.jobName, jobName);
            map.put(CommonConstants.tenantId, TenantHolder.get().getId());
            mapList.add(map);
        });
        ContextBeanAware.getBean(TriggerMapper.class).saveDailyIntervalTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildDailyIntervalTriggers(tenantId, paramOfTriggers);
    }
}
