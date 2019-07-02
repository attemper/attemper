package com.github.attemper.web.util;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import org.quartz.*;
import org.quartz.spi.OperableTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupportUtil {

    public static List<Date> getNextDateList(List<? extends CommonTriggerResult> list, String tenantId) {
        List<Date> nextDateList = new ArrayList<>();
        if (list != null) {
            try {
                for (CommonTriggerResult item : list) {
                    Scheduler scheduler = SpringContextAware.getBean(Scheduler.class);
                    Trigger trigger = scheduler.getTrigger(new TriggerKey(item.getTriggerName(), tenantId));
                    if (trigger != null) {
                        Calendar calendar = trigger.getCalendarName() != null ?
                                scheduler.getCalendar(trigger.getCalendarName()) : null;
                        nextDateList.addAll(TriggerUtils.computeFireTimes((OperableTrigger) trigger, calendar, 10));
                    }
                }
            } catch (SchedulerException e) {
                throw new RTException(6150, e);
            }
        }
        return nextDateList;
    }
}
