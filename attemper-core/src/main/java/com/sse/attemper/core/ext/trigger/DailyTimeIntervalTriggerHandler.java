package com.sse.attemper.core.ext.trigger;

import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;

import java.util.List;
import java.util.Map;

public class DailyTimeIntervalTriggerHandler implements TriggerHandlerInDatabase {

    @Override
    public List getTriggers(Map jobNameWithTenantIdMap) {
        return ContextBeanAware.getBean(TriggerMapper.class).getDailyIntervalTriggers(jobNameWithTenantIdMap);
    }
}
