package com.github.attemper.core.engine.date;

import java.util.Calendar;

public class WeekDate extends DateAdapter {

    @Override
    protected void addPeriodOffset() {
        cal.add(Calendar.WEEK_OF_YEAR, periodOffset);
    }

    @Override
    public void computeRange() {
        Calendar tempCal = copyCalendar(cal);
        tempCal.set(Calendar.DAY_OF_WEEK, tempCal.getFirstDayOfWeek());
        lowerCal = copyCalendar(tempCal);
        tempCal.add(Calendar.DAY_OF_YEAR, 6);
        upperCal = copyCalendar(tempCal);
    }

}
