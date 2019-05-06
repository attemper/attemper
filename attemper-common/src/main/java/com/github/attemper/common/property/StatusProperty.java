package com.github.attemper.common.property;

import com.github.attemper.common.constant.GlobalConstants;

import java.util.ResourceBundle;

public class StatusProperty {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
            GlobalConstants.statusPropertiesName, GlobalConstants.locale);

    public static String getValue(int key) {
        return getValue(String.valueOf(key));
    }

    /**
     * 没有value就返回key
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
}
