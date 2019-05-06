package com.github.attemper.core.ext.trigger;

import com.github.attemper.config.base.bean.ContextBeanAware;
import com.github.attemper.core.dao.mapper.job.TriggerMapper;

import java.util.List;
import java.util.Map;

public class CalendarOffsetTriggerHandler implements TriggerHandlerInDatabase {

    @Override
    public List getTriggers(Map jobNameWithTenantIdMap) {
        return ContextBeanAware.getBean(TriggerMapper.class).getCalendarOffsetTriggers(jobNameWithTenantIdMap);
    }
}
