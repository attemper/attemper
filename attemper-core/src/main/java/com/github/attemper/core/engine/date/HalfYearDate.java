package com.github.attemper.core.engine.date;

import java.util.Calendar;

public class HalfYearDate extends DateAdapter {

    @Override
    protected void addPeriodOffset() {
        cal.add(Calendar.MONTH, periodOffset * 6);
    }

    @Override
    public void computeRange() {
        Calendar tempCal = copyCalendar(cal);
        int startMonth = tempCal.get(Calendar.MONTH)/6 * 6;   // !!! The start month is 0,the end month is 11
        tempCal.set(Calendar.MONTH, startMonth);
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        lowerCal = copyCalendar(tempCal);
        tempCal.add(Calendar.MONTH, 6);
        tempCal.add(Calendar.DATE, -1);
        upperCal = copyCalendar(tempCal);
    }

}
