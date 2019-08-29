package com.github.attemper.executor.store;

import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutorStore {

    /**
     * key: executionId
     * value: EndParam
     */
    private static Map<String, EndParam> endMap = new ConcurrentHashMap<>(8);

    public static Map<String, EndParam> getEndMap() {
        return endMap;
    }
}
