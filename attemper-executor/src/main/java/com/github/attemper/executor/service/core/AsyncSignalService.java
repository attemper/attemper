package com.github.attemper.executor.service.core;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AsyncSignalService {

    public String signal(Map<String, Object> map) {
        String executionId = (String) map.get("executionId");
        synchronized (executionId.intern()) {
            executionId.intern().notify();
        }
        return executionId;
    }
}
