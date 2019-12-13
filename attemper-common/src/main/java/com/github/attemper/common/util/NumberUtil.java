package com.github.attemper.common.util;

import java.text.NumberFormat;

/**
 * util for number
 */
public class NumberUtil {

    public static String toPercentage(long dividend, long divisor) {
        if (dividend <= 0) {
            return "0%";
        }
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(divisor*1.0/dividend);
    }
}
