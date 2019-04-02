package com.sse.attemper.core.ext.trigger;

import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.sdk.common.constant.SdkCommonConstants;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CronTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.sse.attemper.sys.holder.TenantHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.*;

public class CronTriggerHandler implements TriggerHandler<CronTriggerParam, CronTriggerResult> {

    @Override
    public List<CronTriggerResult> getTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        return ContextBeanAware.getBean(TriggerMapper.class).getCronTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        ContextBeanAware.getBean(TriggerMapper.class).deleteCronTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<CronTriggerParam> paramOfTriggers) {
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    item.setTriggerType(Constants.TTYPE_CRON);
                    if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                        item.setTimeZoneId(TimeZone.getDefault().getID());
                    }
                    Map<String, Object> map = BeanUtil.beanToMap(item);
                    map.put(SdkCommonConstants.jobName, jobName);
                    map.put(SdkCommonConstants.tenantId, TenantHolder.get().getId());
                    mapList.add(map);
                });
        ContextBeanAware.getBean(TriggerMapper.class).saveCronTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String jobName, List<CronTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    Trigger trigger = triggerBuilder(item).withSchedule(CronScheduleBuilder.cronSchedule(item.getExpression())
                            .inTimeZone(TimeZone.getTimeZone(item.getTimeZoneId()))).build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }
}
