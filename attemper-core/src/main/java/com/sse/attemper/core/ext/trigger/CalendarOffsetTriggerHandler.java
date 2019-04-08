package com.sse.attemper.core.ext.trigger;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.param.dispatch.trigger.sub.CalendarOffsetTriggerParam;
import com.sse.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.util.QuartzUtil;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.sys.holder.TenantHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(item -> {
            item.setTriggerType(Constants.TTYPE_CAL_OFFSET);
            Map<String, Object> map = BeanUtil.beanToMap(item);
            map.put(CommonConstants.jobName, jobName);
            map.put(CommonConstants.tenantId, TenantHolder.get().getId());
            mapList.add(map);
        });
        ContextBeanAware.getBean(TriggerMapper.class).saveCalendarOffsetTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildCalendarOffsetTriggers(tenantId, paramOfTriggers);
    }

}
