package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarSaveParam;
import com.github.attemper.common.util.DateTimeUtil;
import com.github.attemper.core.dao.dispatch.CalendarMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.web.ext.app.SchedulerHandler;
import org.quartz.Calendar;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class CalendarOperatedService extends BaseServiceAdapter {

    @Autowired
    private CalendarMapper mapper;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SchedulerHandler schedulerHandler;

    public Void saveDay(DayCalendarSaveParam param) {
        mapper.saveDay(injectTenantIdExceptAdminToMap(param));
        saveHolidayCalendar(param.getCalendarName(), param.getDayNum(), false);
        return null;
    }

    public Void removeDay(DayCalendarRemoveParam param) {
        mapper.deleteDay(injectTenantIdExceptAdminToMap(param));
        saveHolidayCalendar(param.getCalendarName(), param.getDayNum(), true);
        return null;
    }

    private void saveHolidayCalendar(String calendarName, int dayNum, boolean toRemove) {
        try {
            Calendar calendar = scheduler.getCalendar(calendarName);
            HolidayCalendar holidayCalendar;
            if (calendar == null) {
                if (toRemove) {
                    throw new RTException(3006, calendarName);
                }
                holidayCalendar = new HolidayCalendar();
            } else {
                holidayCalendar = (HolidayCalendar) calendar;
            }
            Date date = DateTimeUtil.parseDateStr(String.valueOf(dayNum), CommonConstants.yyyyMMdd);
            if (toRemove) {
                holidayCalendar.removeExcludedDate(date);
            } else {
                holidayCalendar.addExcludedDate(date);
            }
            scheduler.addCalendar(calendarName, holidayCalendar, true, true);
            schedulerHandler.saveCalendar(new CalendarGetParam().setCalendarName(calendarName));
        } catch (SchedulerException e) {
            throw new RTException(e);
        }
    }
}
