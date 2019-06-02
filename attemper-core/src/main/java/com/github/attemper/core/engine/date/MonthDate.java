package com.github.attemper.core.engine.date;

import java.util.Calendar;

public class MonthDate extends DateAdapter {

    @Override
    protected void addPeriodOffset() {
        cal.add(Calendar.MONTH, periodOffset);
    }

    @Override
    public void computeRange() {
        Calendar tempCal = copyCalendar(cal);
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        lowerCal = copyCalendar(tempCal);
        tempCal.add(Calendar.MONTH, 1);
        tempCal.add(Calendar.DATE, -1);
        upperCal = copyCalendar(tempCal);
    }

}
