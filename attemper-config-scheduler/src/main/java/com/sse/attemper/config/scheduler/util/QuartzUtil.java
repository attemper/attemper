package com.sse.attemper.config.scheduler.util;

import com.sse.attemper.common.param.dispatch.trigger.sub.*;
import com.sse.attemper.config.scheduler.job.ExecutableJob;
import org.quartz.*;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.*;

/**
 * operate quarzt Job/JobKey/JobDetail/Trigger/TriggerKey...
 */
public class QuartzUtil {

    public static JobKey newJobKey(String jobName, String tenantId) {
        return new JobKey(jobName, tenantId);
    }

    public static JobDetail newJobDetail(String jobName, String tenantId) {
        return JobBuilder.newJob(ExecutableJob.class).withIdentity(jobName, tenantId).build();
    }

    public static <K extends CommonTriggerParam> TriggerBuilder triggerBuilder(String tenantId, K item) {
        return TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey(item.getTriggerName(), tenantId))
                .startAt(item.getStartTime() == null ? initDateAfterTwoSecond() : item.getStartTime())
                .endAt(item.getEndTime())
                .withDescription(item.getDescription());
    }

    public static Set<Trigger> buildCronTriggers(String tenantId, List<CronTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    Trigger trigger = triggerBuilder(tenantId, item).withSchedule(CronScheduleBuilder.cronSchedule(item.getExpression())
                            .inTimeZone(TimeZone.getTimeZone(item.getTimeZoneId()))).build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }

    public static Set<Trigger> buildCalendarOffsetTriggers(String tenantId, List<CalendarOffsetTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    TimeOfDay startTimeOfDay = TimeUtil.toTime(item.getStartTimeOfDay());
                    CalendarOffsetScheduleBuilder scheduleBuilder = CalendarOffsetScheduleBuilder.calendarOffsetSchedule()
                            .withIntervalUnit(DateBuilder.IntervalUnit.valueOf(item.getTimeUnit()))
                            .withRepeatCount(item.getRepeatCount())
                            .withInnerOffset(item.getInnerOffset())
                            .withOuterOffset(item.getOuterOffset())
                            .reversed(item.isReversed());
                    if (startTimeOfDay != null) {
                        scheduleBuilder.startingDailyAt(startTimeOfDay);
                    }
                    Trigger trigger = triggerBuilder(tenantId, item).withSchedule(scheduleBuilder)
                            .build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }

    public static Set<Trigger> buildCalendarIntervalTriggers(String tenantId, List<CalendarIntervalTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    Trigger trigger = triggerBuilder(tenantId, item).withSchedule(
                            CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                                    .inTimeZone(TimeZone.getTimeZone(item.getTimeZoneId()))
                                    .withRepeatCount(item.getRepeatCount())
                                    .preserveHourOfDayAcrossDaylightSavings(item.isPreserveDayLight())
                                    .skipDayIfHourDoesNotExist(item.isSkipDayIfNoHour())
                                    .withInterval(item.getInterval(), DateBuilder.IntervalUnit.valueOf(item.getTimeUnit())))
                            .build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }

    public static Set<Trigger> buildDailyIntervalTriggers(String tenantId, List<DailyIntervalTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    Trigger trigger;
                    if (Constants.TTYPE_SIMPLE.equals(item.getTriggerType())) {
                        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(item.getRepeatCount())
                                .withIntervalInMilliseconds(item.getInterval());
                        trigger = triggerBuilder(tenantId, item).withSchedule(builder).build();
                    } else {
                        DailyTimeIntervalScheduleBuilder builder = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
                                .withInterval(item.getInterval(), DateBuilder.IntervalUnit.valueOf(item.getTimeUnit()))
                                .endingDailyAt(TimeUtil.toTime(item.getEndTimeOfDay()))
                                .withRepeatCount(item.getRepeatCount());
                        TimeOfDay startTimeOfDay = TimeUtil.toTime(item.getStartTimeOfDay());
                        Set<Integer> days = TimeUtil.toDaysOfTheWeek(item.getDaysOfWeek());
                        if (startTimeOfDay != null) {
                            builder.startingDailyAt(startTimeOfDay);
                        }
                        if (days != null) {
                            builder.onDaysOfTheWeek(days);
                        }
                        trigger = triggerBuilder(tenantId, item).withSchedule(builder).build();
                    }
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }

    /**
     * add two second to current time
     *
     * @return
     */
    private static Date initDateAfterTwoSecond() {
        long mills = System.currentTimeMillis();
        return new Date(mills + 2 * 1000);
    }
}
