package com.github.attemper.common.base;

import java.util.Map;

/**
 * @author ldang
 */
public interface BaseMapper<T> {

    void add(T t);

    void update(T t);

    void add(Map<String, Object> paramMap);

    void update(Map<String, Object> paramMap);
}
