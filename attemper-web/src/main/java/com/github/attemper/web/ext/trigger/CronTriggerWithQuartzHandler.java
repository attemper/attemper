package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.dispatch.trigger.sub.CronTriggerParam;
import com.github.attemper.common.result.dispatch.trigger.sub.CronTriggerResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.dispatch.TriggerMapper;
import com.github.attemper.core.ext.trigger.CronTriggerHandler;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.sys.holder.TenantHolder;
import org.apache.commons.lang.StringUtils;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.*;

public class CronTriggerWithQuartzHandler extends CronTriggerHandler implements TriggerWithQuartzHandler<CronTriggerParam, CronTriggerResult> {

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        SpringContextAware.getBean(TriggerMapper.class).deleteCronTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<CronTriggerParam> paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.isEmpty()) {
            return;
        }
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    item.setTriggerType(Constants.TTYPE_CRON);
                    if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                        item.setTimeZoneId(TimeZone.getDefault().getID());
                    }
                    Map<String, Object> map = BeanUtil.bean2Map(item);
                    map.put(CommonConstants.jobName, jobName);
                    map.put(CommonConstants.tenantId, TenantHolder.get().getUserName());
                    mapList.add(map);
                });
        SpringContextAware.getBean(TriggerMapper.class).saveCronTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String tenantId, List paramOfTriggers) {
        return QuartzUtil.buildCronTriggers(tenantId, paramOfTriggers);
    }
}
