package com.github.attemper.executor.service.core;

import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.executor.store.ExecutorStore;
import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignalService {

    @Autowired
    private InstanceService service;

    public Void signal(EndParam endParam) {
        ExecutorStore.getEndMap().put(endParam.getBaseExecutionParam().getExecutionId(), endParam);
        String actInstId = endParam.getBaseExecutionParam().getActInstId();
        synchronized (actInstId.intern()) { // unlock
            actInstId.intern().notify();
        }
        return null;
    }
}
