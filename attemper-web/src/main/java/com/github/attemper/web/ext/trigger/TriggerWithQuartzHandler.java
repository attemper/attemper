package com.github.attemper.web.ext.trigger;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.trigger.sub.CommonTriggerWrapper;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.invoker.util.QuartzUtil;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.triggers.AbstractTrigger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TriggerWithQuartzHandler<T extends CommonTriggerWrapper> {

    T getSpecifyTrigger(Trigger trigger);

    Set<Trigger> buildTriggers(String tenantId, List<T> paramOfTriggers);

    default T getTrigger(Trigger trigger) {
        T t = getSpecifyTrigger(trigger);
        setCommonProperty(t, trigger);
        return t;
    }

    default void setCommonProperty(CommonTriggerWrapper commonTriggerWrapper, Trigger trigger) {
        AbstractTrigger<Trigger> abstractTrigger = (AbstractTrigger) trigger;
        commonTriggerWrapper.setTriggerName(abstractTrigger.getName());
        commonTriggerWrapper.setStartTime(abstractTrigger.getStartTime() != null ? abstractTrigger.getStartTime().getTime() : null);
        commonTriggerWrapper.setEndTime(abstractTrigger.getEndTime() != null ? abstractTrigger.getEndTime().getTime() : null);
        commonTriggerWrapper.setMisfireInstruction(abstractTrigger.getMisfireInstruction());
        commonTriggerWrapper.setCalendarNames(abstractTrigger.getCalendarName() == null ? null : Arrays.asList(abstractTrigger.getCalendarName().split(CommonConstants.COLON)));
    }

    default void schedule(String jobName, String tenantId, Map<String, Object> jobDataMap, List paramOfTriggers) {
        if (paramOfTriggers == null || paramOfTriggers.size() == 0) {
            return;
        }
        Set<Trigger> quartzTriggers = buildTriggers(tenantId, paramOfTriggers);
        JobDetail jobDetail = QuartzUtil.newJobDetail(jobName, tenantId, jobDataMap);
        try {
            SpringContextAware.getBean(Scheduler.class).scheduleJob(jobDetail, quartzTriggers, true);
        } catch (SchedulerException e) {
            throw new RTException(3003, e);
        }
    }
}
