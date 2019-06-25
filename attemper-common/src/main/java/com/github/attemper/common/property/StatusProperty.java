package com.github.attemper.common.property;

import com.github.attemper.common.constant.GlobalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.ResourceBundle;

public class StatusProperty {

    private static final Logger log = LoggerFactory.getLogger(StatusProperty.class);

    private static ResourceBundle resourceBundle;

    static {
        try {
            resourceBundle = getResourceBundleByLocale(Locale.getDefault());
            if (resourceBundle == null) {
                if (Locale.getDefault().equals(Locale.ENGLISH)) {
                    resourceBundle = getResourceBundleByLocale(Locale.US);
                } else {
                    resourceBundle = getResourceBundleByLocale(Locale.SIMPLIFIED_CHINESE);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resourceBundle = getResourceBundleByLocale(Locale.SIMPLIFIED_CHINESE);
        }
    }

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

    private static ResourceBundle getResourceBundleByLocale(Locale locale) {
        return ResourceBundle.getBundle(
                GlobalConstants.statusPropertiesName, locale);
    }
}
