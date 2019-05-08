package com.github.attemper.common.base;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
public interface BaseMapper<T> {

    void add(T t);

    void update(T t);

    T get(Map<String, Object> paramMap);

    List<T> list(Map<String, Object> paramMap);

    void add(Map<String, Object> paramMap);

    void update(Map<String, Object> paramMap);

    void save(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}
