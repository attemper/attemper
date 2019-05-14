package com.github.attemper.executor.service.core;

import com.github.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.github.attemper.core.dao.mapper.monitor.JobInstanceMapper;
import com.github.attemper.java.sdk.common.biz2executor.param.end.EndExecutionParam;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignalService {

    @Autowired
    private JobInstanceMapper mapper;

    public Void signal(EndExecutionParam endExecutionParam) {
        String actInstId = endExecutionParam.getBaseExecutionParam().getActInstId();
        synchronized (actInstId.intern()) { // unlock
            actInstId.intern().notify();
        }
        TaskResult taskResult = endExecutionParam.getTaskResult();
        if (taskResult != null) {
            if (StringUtils.isNotBlank(taskResult.getLogKey()) || StringUtils.isNotBlank(taskResult.getLogText())) {
                JobInstanceAct jobInstanceAct = JobInstanceAct.builder()
                        .actInstId(actInstId)
                        .logKey(taskResult.getLogKey())
                        .logText(taskResult.getLogText())
                        .build();
                mapper.appendLog(jobInstanceAct);
            }
        }
        return null;
    }
}
