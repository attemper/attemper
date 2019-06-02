package com.github.attemper.core.engine.date;

import java.util.Calendar;

public class YearDate extends DateAdapter{

    @Override
    protected void addPeriodOffset() {
        cal.add(Calendar.YEAR, periodOffset);
    }

    @Override
    public void computeRange() {
        Calendar tempCal = copyCalendar(cal);
        tempCal.set(Calendar.MONTH, 0);
        tempCal.set(Calendar.DATE, 1);
        lowerCal = copyCalendar(tempCal);
        tempCal.add(Calendar.DAY_OF_YEAR, 6);
        upperCal = copyCalendar(tempCal);
    }
}
