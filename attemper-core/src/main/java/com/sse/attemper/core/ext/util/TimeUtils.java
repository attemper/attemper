package com.sse.attemper.core.ext.util;

import org.quartz.TimeOfDay;

/**
 * utils of time
 */
public class TimeUtils {

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
}
