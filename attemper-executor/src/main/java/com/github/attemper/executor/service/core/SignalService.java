package com.github.attemper.executor.service.core;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignalService {

    @Autowired
    private JobInstanceService service;

    public Void signal(EndParam endParam) {
        String actInstId = endParam.getBaseExecutionParam().getActInstId();
        TaskResult taskResult = endParam.getTaskResult();
        if (taskResult != null) {
            if (taskResult.getSuccess()) {
                saveInstanceAct(actInstId, taskResult.getLogKey(), taskResult.getLogText(), JobInstanceStatus.SUCCESS);
            } else {
                saveInstanceAct(actInstId, taskResult.getLogKey(), taskResult.getLogText(), JobInstanceStatus.FAILURE);
            }
        } else {
            saveInstanceAct(actInstId, null, null, JobInstanceStatus.SUCCESS);
        }
        synchronized (actInstId.intern()) { // unlock
            actInstId.intern().notify();
        }
        return null;
    }

    protected void saveInstanceAct(String actInstId, String logKey, String logText, JobInstanceStatus jobInstanceStatus) {
        JobInstanceAct jobInstanceAct = JobInstanceAct.builder()
                .actInstId(actInstId)
                .logKey(logKey)
                .logText(logText)
                .status(jobInstanceStatus.getStatus())
                .build();
        service.updateAct(jobInstanceAct);
    }
}
