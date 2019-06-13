package com.github.attemper.scheduler.handler;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.quartz.Calendar;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarUpdatedService extends BaseServiceAdapter {

    @Autowired
    private Scheduler scheduler;

    public Void saveCalendar(CalendarGetParam param) {
        try {
            Calendar calendar = scheduler.getCalendar(param.getCalendarName());
            scheduler.addCalendarInMemory(param.getCalendarName(), calendar, true, true);
        } catch (SchedulerException e) {
            throw new RTException(3007, e);
        }
        return null;
    }

}
