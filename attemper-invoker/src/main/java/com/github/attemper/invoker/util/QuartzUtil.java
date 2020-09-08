package com.github.attemper.invoker.util;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.calendar.CalendarNameParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarListParam;
import com.github.attemper.common.param.dispatch.trigger.sub.*;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.common.util.DateTimeUtil;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.dispatch.CalendarService;
import com.github.attemper.invoker.job.ExecutableJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Calendar;
import org.quartz.*;
import org.quartz.impl.calendar.HolidayCalendar;
import org.quartz.impl.jdbcjobstore.Constants;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.spi.OperableTrigger;

import java.util.*;

/**
 * operate quartz Job/JobKey/JobDetail/Trigger/TriggerKey...
 */
@Slf4j
public class QuartzUtil {

    private static final int numTimes = 10;

    public static JobDetail newJobDetail(String jobName, String tenantId, Map<String, Object> jobDataMap) {
        JobBuilder jobBuilder = JobBuilder.newJob(ExecutableJob.class).withIdentity(jobName, tenantId);
        if (jobDataMap != null) {
            jobBuilder.setJobData(new JobDataMap(jobDataMap));
        }
        return jobBuilder.build();
    }

    public static <K extends CommonTriggerWrapper> TriggerBuilder<Trigger> buildTrigger(String tenantId, K item) {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(item.getTriggerName(), tenantId))
                .startAt(preHandleDate(item.getStartTime()))
                .endAt(item.getEndTime() == null ? null : new Date(item.getEndTime()));
        String calendarName = handleHolidayCalendar(item.getCalendarNames());
        triggerBuilder.modifiedByCalendar(calendarName);
        return triggerBuilder;
    }

    public static Set<Trigger> buildCronTriggers(String tenantId, List<CronTriggerWrapper> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet<>(paramOfTriggers.size());
        for (CronTriggerWrapper item : paramOfTriggers) {
            if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                item.setTimeZoneId(TimeZone.getDefault().getID());
            }
            CronScheduleBuilder cronScheduleBuilder;
            try {
                cronScheduleBuilder = CronScheduleBuilder.cronSchedule(item.getExpression());
            } catch (Exception e) {
                throw new RTException(6122, e.getMessage());
            }
            AbstractTrigger<CronTrigger> trigger = (AbstractTrigger<CronTrigger>) buildTrigger(tenantId, item)
                .withSchedule(cronScheduleBuilder.inTimeZone(TimeZone.getTimeZone(item.getTimeZoneId()))).build();
            trigger.setMisfireInstruction(item.getMisfireInstruction());
            quartzTriggers.add(trigger);
        }
        return quartzTriggers;
    }

    public static Set<Trigger> buildCalendarOffsetTriggers(String tenantId, List<CalendarOffsetTriggerWrapper> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet<>(paramOfTriggers.size());
        for (CalendarOffsetTriggerWrapper item : paramOfTriggers) {
            TimeOfDay startTimeOfDay = TimeUtil.toTimeOfDay(item.getStartTimeOfDay());
            CalendarOffsetScheduleBuilder scheduleBuilder = CalendarOffsetScheduleBuilder.calendarOffsetSchedule()
                    .withIntervalUnit(DateBuilder.IntervalUnit.valueOf(item.getTimeUnit()))
                    .withRepeatCount(item.getRepeatCount())
                    .withInnerOffset(item.getInnerOffset())
                    .withOuterOffset(item.getOuterOffset())
                    .reversed(item.getReversed() != 0);
            if (startTimeOfDay != null) {
                scheduleBuilder.startingDailyAt(startTimeOfDay);
            }
            AbstractTrigger<CalendarOffsetTrigger> trigger = (AbstractTrigger<CalendarOffsetTrigger>) buildTrigger(tenantId, item)
                    .withSchedule(scheduleBuilder).build();
            trigger.setMisfireInstruction(item.getMisfireInstruction());
            quartzTriggers.add(trigger);
        }
        return quartzTriggers;
    }

    public static Set<Trigger> buildCalendarIntervalTriggers(String tenantId, List<CalendarIntervalTriggerWrapper> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet<>(paramOfTriggers.size());
        for (CalendarIntervalTriggerWrapper item : paramOfTriggers) {
            if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                item.setTimeZoneId(TimeZone.getDefault().getID());
            }
            AbstractTrigger<CalendarIntervalTrigger> trigger = (AbstractTrigger<CalendarIntervalTrigger>) buildTrigger(tenantId, item).withSchedule(
                    CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                            .inTimeZone(TimeZone.getTimeZone(item.getTimeZoneId()))
                            .withRepeatCount(item.getRepeatCount())
                            .preserveHourOfDayAcrossDaylightSavings(item.getPreserveDayLight() != 0)
                            .skipDayIfHourDoesNotExist(item.getSkipDayIfNoHour() != 0)
                            .withInterval(item.getRepeatInterval(), DateBuilder.IntervalUnit.valueOf(item.getTimeUnit())))
                    .build();
            trigger.setMisfireInstruction(item.getMisfireInstruction());
            quartzTriggers.add(trigger);
        }
        return quartzTriggers;
    }

    public static Set<Trigger> buildDailyIntervalTriggers(String tenantId, List<DailyTimeIntervalTriggerWrapper> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet<>(paramOfTriggers.size());
        for (DailyTimeIntervalTriggerWrapper item : paramOfTriggers) {
            AbstractTrigger trigger;
            if (Constants.TTYPE_SIMPLE.equals(item.getTriggerType())) {
                SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(item.getRepeatCount())
                        .withIntervalInMilliseconds(item.getRepeatInterval());
                trigger = (AbstractTrigger<SimpleTrigger>) buildTrigger(tenantId, item).withSchedule(builder).build();
            } else {
                DailyTimeIntervalScheduleBuilder builder = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
                        .withInterval(item.getRepeatInterval(), DateBuilder.IntervalUnit.valueOf(item.getTimeUnit()))
                        .endingDailyAt(TimeUtil.toTimeOfDay(item.getEndTimeOfDay()))
                        .withRepeatCount(item.getRepeatCount());
                TimeOfDay startTimeOfDay = TimeUtil.toTimeOfDay(item.getStartTimeOfDay());
                Set<Integer> days = TimeUtil.toDaysOfTheWeek(item.getDaysOfWeek());
                if (startTimeOfDay != null) {
                    builder.startingDailyAt(startTimeOfDay);
                }
                if (days != null) {
                    builder.onDaysOfTheWeek(days);
                }
                trigger = (AbstractTrigger<DailyTimeIntervalTrigger>) buildTrigger(tenantId, item).withSchedule(builder).build();
            }
            trigger.setMisfireInstruction(item.getMisfireInstruction());
            quartzTriggers.add(trigger);
        }
        return quartzTriggers;
    }

    private static String handleHolidayCalendar(List<String> calendarNames) {
        if (calendarNames == null || calendarNames.size() == 0) {
            return null;
        } else if (calendarNames.size() == 1) {
            getAndAddHolidayCalendar(calendarNames.get(0));
            return calendarNames.get(0);
        } else {
            HolidayCalendar lastCalendar = getAndAddHolidayCalendar(calendarNames.get(0));
            HolidayCalendar currentCalendar = null;
            for (int i = 1; i < calendarNames.size(); i++) {
                currentCalendar = getAndAddHolidayCalendar(calendarNames.get(i));
                currentCalendar.setBaseCalendar(lastCalendar);
                if (i == calendarNames.size() - 1) {
                    break;
                }
                lastCalendar = (HolidayCalendar) currentCalendar.clone();
            }
            String combinedCalendarName = StringUtils.join(calendarNames, CommonConstants.COLON);
            Scheduler scheduler = SpringContextAware.getBean(Scheduler.class);
            try {
                if (scheduler.getCalendar(combinedCalendarName) == null) {
                    scheduler.addCalendar(combinedCalendarName, currentCalendar, false, false);
                }
            } catch (SchedulerException e) {
                throw new RTException(e);
            }
            return combinedCalendarName;
        }
    }

    public static HolidayCalendar getAndAddHolidayCalendar(String calendarName) {
        Scheduler scheduler = SpringContextAware.getBean(Scheduler.class);
        try {
            HolidayCalendar holidayCalendar = (HolidayCalendar) scheduler.getCalendar(calendarName);
            if (holidayCalendar == null) {
                holidayCalendar = getHolidayCalendar(calendarName);
                scheduler.addCalendar(calendarName, holidayCalendar, false, false);
            }
            return holidayCalendar;
        } catch (SchedulerException e) {
            throw new RTException(e);
        }
    }

    public static HolidayCalendar getHolidayCalendar(String calendarName) {
        HolidayCalendar holidayCalendar = new HolidayCalendar();
        CalendarService calendarService = SpringContextAware.getBean(CalendarService.class);
        CalendarInfo calendarInfo = calendarService.get(new CalendarNameParam().setCalendarName(calendarName));
        if (calendarInfo == null) {
            throw new RTException(3006, calendarName);
        }
        DayCalendarListParam listParam = new DayCalendarListParam();
        listParam.setCalendarName(calendarName).setPageSize(100000);
        List<DayCalendarConfig> dayCalendarConfigs = calendarService.listDay(listParam);
        for (DayCalendarConfig item : dayCalendarConfigs) {
            Integer dayNum = item.getDayNum();
            if (dayNum == null) {
                throw new RTException(6705);
            }
            Date date = DateTimeUtil.parseDateStr(String.valueOf(dayNum), CommonConstants.yyyyMMdd);
            if (date == null) {
                throw new RTException(6705);
            } else {
                holidayCalendar.addExcludedDate(date);
            }
        }
        return holidayCalendar;
    }

    private static HolidayCalendar getHolidayCalendar(List<String> calendarNames) {
        if (calendarNames == null || calendarNames.size() == 0) {
            return null;
        } else if (calendarNames.size() == 1) {
            return getHolidayCalendar(calendarNames.get(0));
        } else {
            HolidayCalendar lastCalendar = getHolidayCalendar(calendarNames.get(0));
            HolidayCalendar currentCalendar = null;
            for (int i = 1; i < calendarNames.size(); i++) {
                currentCalendar = getHolidayCalendar(calendarNames.get(i));
                currentCalendar.setBaseCalendar(lastCalendar);
                if (i == calendarNames.size() - 1) {
                    break;
                }
                lastCalendar = (HolidayCalendar) currentCalendar.clone();
            }
            return currentCalendar;
        }
    }

    public static List<Date> getNextFireTimes(List<? extends CommonTriggerWrapper> list, String tenantId) {
        if (list != null && list.size() > 0) {
            List<Date> allNextFireTimes = new ArrayList<>(numTimes);
            for (CommonTriggerWrapper item : list) {
                allNextFireTimes.addAll(getNextFireTimes(item.getTriggerName(), tenantId));
            }
            return allNextFireTimes;
        }
        return new ArrayList<>(0);
    }

    public static List<Date> getNextFireTimes(String triggerName, String tenantId) {
        List<Date> nextFireTimes = new ArrayList<>(numTimes);
        try {
            Scheduler scheduler = SpringContextAware.getBean(Scheduler.class);
            Trigger trigger = scheduler.getTrigger(new TriggerKey(triggerName, tenantId));
            if (trigger != null) {
                Calendar calendar = trigger.getCalendarName() != null ?
                        scheduler.getCalendar(trigger.getCalendarName()) : null;
                nextFireTimes.addAll(resolvePastTime(TriggerUtils.computeFireTimes((OperableTrigger) trigger, calendar, numTimes)));
            }
        } catch (SchedulerException e) {
            throw new RTException(6150, e);
        }
        return nextFireTimes;
    }

    public static List<Date> getNextFireTimes(Set<Trigger> triggers, List<String> calendarNames) {
        HolidayCalendar holidayCalendar = getHolidayCalendar(calendarNames);
        for (Trigger trigger : triggers) {
            return resolvePastTime(TriggerUtils.computeFireTimes((OperableTrigger) trigger, holidayCalendar, numTimes));
        }
        return null;
    }

    private static List<Date> resolvePastTime(List<Date> dates) {
        Date now = new Date();
        if (dates == null || dates.size() == 0) {
            return dates;
        }
        List<Date> dateList = new ArrayList<>(dates.size());
        for (Date date : dates) {
            if (!date.before(now)) {  // only show future date
                dateList.add(date);
            }
        }
        return dateList;
    }

    /**
     * add 5 second to current time
     *
     * @return
     */
    private static Date preHandleDate(Long timeMills) {
        long mills = System.currentTimeMillis();
        if (timeMills == null || timeMills < mills + 5 * 1000) {
            return new Date(mills + 5 * 1000);
        }
        return new Date(timeMills);
    }
}
