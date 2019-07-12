package com.github.attemper.executor.task;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.core.service.instance.JobInstanceService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Date;

public abstract class ParentTask {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected JobInstanceService jobInstanceService;

    protected void saveInstanceAct(DelegateExecution execution, String url, String logKey, String logText, JobInstanceStatus jobInstanceStatus) {
        JobInstanceAct jobInstanceAct = jobInstanceService.getAct(execution.getActivityInstanceId());
        Date now = new Date();
        jobInstanceAct.setBizUri(url);
        jobInstanceAct.setEndTime(now);
        jobInstanceAct.setDuration(now.getTime() - jobInstanceAct.getStartTime().getTime());
        jobInstanceAct.setLogKey(logKey);
        jobInstanceAct.setLogText(logText);
        if (jobInstanceStatus != null) {
            jobInstanceAct.setStatus(jobInstanceStatus.getStatus());
        }
        jobInstanceService.updateAct(jobInstanceAct);

        saveInstance(execution, logKey, logText, jobInstanceStatus);
    }

    protected void saveInstance(DelegateExecution execution, String logKey, String logText, JobInstanceStatus jobInstanceStatus) {
        if (jobInstanceStatus != null && JobInstanceStatus.FAILURE.getStatus() == jobInstanceStatus.getStatus()) {
            Date now = new Date();
            JobInstance jobInstance = jobInstanceService.get(execution.getBusinessKey());
            jobInstance.setEndTime(now);
            jobInstance.setDuration(now.getTime() - jobInstance.getStartTime().getTime());
            jobInstance.setStatus(JobInstanceStatus.FAILURE.getStatus());
            int code;
            try {
                code = Integer.parseInt(logKey);
                jobInstance.setMsg(logText);
            } catch (Exception e) {
                code = HttpStatus.INTERNAL_SERVER_ERROR.value();
                jobInstance.setMsg(logText + "\n" + e.getMessage());
            }
            jobInstance.setCode(code);
            jobInstanceService.update(jobInstance);
        }
    }
}
