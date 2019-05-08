package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.dispatch.trigger.sub.CalendarOffsetTriggerParam;
import com.github.attemper.common.result.dispatch.trigger.sub.CalendarOffsetTriggerResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.config.scheduler.util.QuartzUtil;
import com.github.attemper.core.dao.mapper.job.TriggerMapper;
import com.github.attemper.core.ext.trigger.CalendarOffsetTriggerHandler;
import com.github.attemper.sys.holder.TenantHolder;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalendarOffsetTriggerWithQuartzHandler extends CalendarOffsetTriggerHandler implements TriggerWithQuartzHandler<CalendarOffsetTriggerParam, CalendarOffsetTriggerResult> {

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        SpringContextAware.getBean(TriggerMapper.class).deleteCalendarOffsetTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<CalendarOffsetTriggerParam> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(item -> {
            item.setTriggerType(Constants.TTYPE_CAL_OFFSET);
            Map<String, Object> map = BeanUtil.bean2Map(item);
            map.put(CommonConstants.jobName, jobName);
            map.put(CommonConstants.tenantId, TenantHolder.get().getId());
            mapList.add(map);
        });
        SpringContextAware.getBean(TriggerMapper.class).saveCalendarOffsetTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildCalendarOffsetTriggers(tenantId, paramOfTriggers);
    }

}
