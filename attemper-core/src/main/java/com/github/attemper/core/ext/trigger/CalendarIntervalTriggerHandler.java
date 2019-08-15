package com.github.attemper.core.ext.trigger;

import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.dao.dispatch.TriggerMapper;

import java.util.List;
import java.util.Map;

public class CalendarIntervalTriggerHandler implements TriggerHandlerInDatabase {

    @Override
    public List getTriggers(Map jobNameWithTenantIdMap) {
        return SpringContextAware.getBean(TriggerMapper.class).getCalendarIntervalTriggers(jobNameWithTenantIdMap);
    }
}
