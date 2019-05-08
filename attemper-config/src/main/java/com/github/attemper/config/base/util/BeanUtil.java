package com.github.attemper.config.base.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.config.base.bean.SpringContextAware;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class BeanUtil {

    public static Map<String, Object> bean2Map(Object obj) {
        ObjectMapper objectMapper = SpringContextAware.getBean(ObjectMapper.class);
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(obj), Map.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RTException(500, String.valueOf(obj));
        }
    }

    public static <T> T map2Bean (Class<T> t, Map<String, Object> map) {
        ObjectMapper objectMapper = SpringContextAware.getBean(ObjectMapper.class);
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(map), t);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RTException(500, String.valueOf(map));
        }
    }
}
