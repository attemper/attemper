package com.github.attemper.executor.service.log;

import com.github.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.github.attemper.core.dao.mapper.monitor.JobInstanceMapper;
import com.github.attemper.java.sdk.common.biz2executor.param.log.LogParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogService {

    @Autowired
    private JobInstanceMapper mapper;

    public Void appendLog(LogParam logParam) {
        JobInstanceAct jobInstanceAct = JobInstanceAct.builder()
                .actInstId(logParam.getBaseExecutionParam().getActInstId())
                .logKey(logParam.getLogResult().getLogKey())
                .logText(logParam.getLogResult().getLogText())
                .build();
        mapper.appendLog(jobInstanceAct);
        return null;
    }

}
