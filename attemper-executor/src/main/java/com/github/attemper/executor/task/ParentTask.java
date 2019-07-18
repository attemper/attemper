package com.github.attemper.executor.task;

import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.core.service.instance.JobInstanceService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ParentTask {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected JobInstanceService jobInstanceService;

    protected void saveLogKey(DelegateExecution execution, String logKey) {
        JobInstanceAct jobInstanceAct = jobInstanceService.getAct(execution.getActivityInstanceId());
        jobInstanceAct.setLogKey(logKey);
        jobInstanceService.updateAct(jobInstanceAct);
    }

    protected void saveUrl(DelegateExecution execution, String url) {
        JobInstanceAct jobInstanceAct = jobInstanceService.getAct(execution.getActivityInstanceId());
        jobInstanceAct.setBizUri(url);
        jobInstanceService.updateAct(jobInstanceAct);
    }
}
