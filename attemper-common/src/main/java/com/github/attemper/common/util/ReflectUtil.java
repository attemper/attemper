package com.github.attemper.common.util;

import com.github.attemper.common.exception.RTException;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectUtil {

    public static Object reflectObj(Class<?> clazz, String prefix, Map<String, Object> map) {
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RTException(1101, e);
        } catch (IllegalAccessException e) {
            throw new RTException(1102, e);
        }
        prefix = StringUtils.trimToNull(prefix);
        setFields(obj, clazz, prefix, map);
        return obj;
    }

    public static Object reflectObj(Class<?> clazz, Map<String, Object> map) {
        return reflectObj(clazz, null, map);
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
                    String realFieldName;
                    if (prefix == null) {
                        realFieldName = field.getName();
                    } else {
                        realFieldName = prefix + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    }
                    if (realFieldName.equals(entry.getKey())) {
                        field.set(obj, entry.getValue());
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
