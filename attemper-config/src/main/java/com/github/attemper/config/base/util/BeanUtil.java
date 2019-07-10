package com.github.attemper.config.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.config.base.bean.SpringContextAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BeanUtil {

    public static Map<String, Object> bean2Map(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }
        try {
            return injectObjectMapper().readValue(bean2JsonStr(obj), Map.class);
        } catch (IOException e) {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), String.valueOf(obj));
        }
    }

    public static <T> T map2Bean (Class<T> t, Map<String, Object> map) {
        try {
            return injectObjectMapper().readValue(bean2JsonStr(map), t);
        } catch (IOException e) {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), String.valueOf(map));
        }
    }

    public static String bean2JsonStr(Object obj) {
        try {
            return injectObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), String.valueOf(obj));
        }
    }

    private static ObjectMapper injectObjectMapper() {
        return SpringContextAware.getBean(ObjectMapper.class);
    }
}
