package com.thor.common.property;

import com.thor.common.constant.GlobalConstants;

import java.util.ResourceBundle;

public class CommonProperty {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
            GlobalConstants.messagesPropertiesName, GlobalConstants.locale);

    /**
     * 没有value就返回key
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if(key != null && resourceBundle.containsKey(key)){
            return resourceBundle.getString(key);
        }
        return key;
    }

}
