package com.github.attemper.common.util;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectUtil {

    public static <T> T reflectObj(T obj, String prefix, Map<String, Object> map) {
        prefix = StringUtils.trimToNull(prefix);
        setFields(obj, obj.getClass(), prefix, map);
        return obj;
    }

    public static <T> T reflectObj(T obj, Map<String, Object> map) {
        return reflectObj(obj, null, map);
    }

    private static void setFields(Object obj, Class<?> clazz, String prefix, Map<String, Object> map) {
        if (clazz == null) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String argName;
                    if (prefix == null) {
                        argName = field.getName();
                    } else {
                        // {prefix}Abc
                        argName = prefix + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    }
                    if (argName.equals(entry.getKey())) {
                        field.set(obj, entry.getValue());
                    }else {
                        // {prefix}_abc
                        argName = prefix + CommonConstants.UNDERSCORE + field.getName();
                        if (argName.equals(entry.getKey())) {
                            field.set(obj, entry.getValue());
                        } else {
                            // {prefix}abc
                            argName = prefix + field.getName();
                            if (argName.equals(entry.getKey())) {
                                field.set(obj, entry.getValue());
                            }
                        }
                    }
                }
                field.setAccessible(accessible);
            }
            setFields(obj, clazz.getSuperclass(), prefix, map);
        } catch (IllegalAccessException e) {
            throw new RTException(1102, e);
        }
    }
}
