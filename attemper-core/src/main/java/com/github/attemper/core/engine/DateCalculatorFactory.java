package com.github.attemper.core.engine;

import com.github.attemper.common.enums.PredefinedDateArgType;
import com.github.attemper.core.engine.date.*;

public class DateCalculatorFactory {

    public static DateHandler getDateHandler(String timeUnit) {
        switch (PredefinedDateArgType.get(timeUnit)) {
            case TODAY:
                return new TradeDate();
            case WEEK:
                return new WeekDate();
            case MONTH:
                return new MonthDate();
            case SEASON:
                return new SeasonDate();
            case HALF_YEAR:
                return new HalfYearDate();
            case YEAR:
                return new YearDate();
            default:
                return null;
        }
    }

}
