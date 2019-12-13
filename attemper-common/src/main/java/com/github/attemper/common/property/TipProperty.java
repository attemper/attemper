package com.github.attemper.common.property;

import java.util.Locale;
import java.util.ResourceBundle;

public class TipProperty {

    private static ResourceBundle resourceBundle;

    public static String getValue(int key) {
        return getValue(String.valueOf(key));
    }

    /**
     *  if value is non-existent ,return key
     * @param key
     * @return
     */
    public static String getValue(String key) {
        try{
            return resourceBundle.getString(key);
        }catch (RuntimeException e) {
            return key;
        }
    }

    public static void initResourceBundle(String language, String country) {
        resourceBundle = ResourceBundle.getBundle(
                "tip", new Locale(language, country));
    }
}
