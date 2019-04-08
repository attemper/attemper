package com.sse.attemper.config.util;

import org.apache.commons.lang.StringUtils;
import org.quartz.TimeOfDay;

import java.util.HashSet;
import java.util.Set;

/**
 * utils of time
 */
public class TimeUtil {

    /**
     * convert time string to TimeOfDay
     * @param timeStr like 01:01:01
     * @return
     */
    public static TimeOfDay toTime(String timeStr) {
        if (timeStr == null) {
            return null;
        }
        String[] timeValues = timeStr.split(":");
        if (timeValues.length != 3) {
            return null;
        }
        return new TimeOfDay(Integer.parseInt(timeValues[0]), Integer.parseInt(timeValues[1]), Integer.parseInt(timeValues[2]));
    }

    public static Set<Integer> toDaysOfTheWeek(String daysOfWeek) {
        if (StringUtils.isBlank(daysOfWeek)) {
            return null;
        }
        String[] daysStr = daysOfWeek.split(",");
        Set<Integer> days = new HashSet<>(daysStr.length);
        for (String s : daysStr) {
            days.add(Integer.parseInt(s));
        }
        return days;
    }
}
