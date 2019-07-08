package com.github.attemper.executor.service.log;

import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.java.sdk.common.executor.param.log.LogParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogService {

    @Autowired
    private JobInstanceService service;

    public Void appendLog(LogParam logParam) {
        JobInstanceAct jobInstanceAct = new JobInstanceAct()
                .setActInstId(logParam.getBaseExecutionParam().getActInstId())
                .setLogKey(logParam.getLogResult().getLogKey())
                .setLogText(logParam.getLogResult().getLogText());
        service.updateAct(jobInstanceAct);
        return null;
    }

}
