package com.github.attemper.core.engine.date;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TradeDate extends DateAdapter {

    @Override
    public Integer calculateTradeDate() {
        int offset = periodOffset + dayOrder;
        if (offset == 0) {
            return getDate(new Date());
        }
        Calendar tempCal = copyCalendar(cal);
        if (offset > 0) {
            tempCal.add(Calendar.DAY_OF_YEAR, 1);
            lowerCal = copyCalendar(tempCal);
            tempCal.add(Calendar.YEAR, 1);
            upperCal = copyCalendar(tempCal);
        } else {
            tempCal.add(Calendar.DAY_OF_YEAR, -1);
            upperCal = copyCalendar(tempCal);
            tempCal.add(Calendar.YEAR, -1);
            lowerCal = copyCalendar(tempCal);
        }
        List<Integer> excludedDates = getExcludedDates();
        if (excludedDates.isEmpty()) {
            cal.add(Calendar.DAY_OF_YEAR, offset);
            return getDate(cal.getTime());
        }
        if (offset > 0){
            Calendar currentCal = copyCalendar(lowerCal);
            while (!currentCal.getTime().after(upperCal.getTime())) {
                int current = getDate(currentCal.getTime());
                if (!excludedDates.contains(current)) {
                    offset--;
                    if (offset == 0) {
                        return current;
                    }
                }
                currentCal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else {
            Calendar currentCal = copyCalendar(upperCal);
            while (!currentCal.getTime().before(lowerCal.getTime())) {
                int current = getDate(currentCal.getTime());
                if (!excludedDates.contains(current)) {
                    offset++;
                    if (offset == 0) {
                        return current;
                    }
                }
                currentCal.add(Calendar.DAY_OF_YEAR, -1);
            }
        }
        return null;
    }

    @Override
    protected void addPeriodOffset() {}

    @Override
    public void computeRange() {}
}
