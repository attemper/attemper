package com.github.attemper.common.param.dispatch.arg.ext;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

@ToString
public class TradeDateArgParam implements CommonParam {

    private static Pattern tradeDatePattern;

    protected String calendarName;

    protected String expression;

    @Override
    public String validate() {
        if (StringUtils.isNotBlank(expression)) {
            if (tradeDatePattern == null) {
                tradeDatePattern = Pattern.compile(CommonConstants.REGEX_TRADE_DATE);
            }
            if (!tradeDatePattern.matcher(expression).find()) {
                return "7040";
            }
        } else {
            return "7040";
        }
        return null;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public TradeDateArgParam setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public TradeDateArgParam setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
