package com.github.attemper.executor.service.core;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.param.dispatch.instance.JobInstanceIdParam;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.core.service.instance.JobInstanceService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobInstanceUpdatedService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private JobInstanceService jobInstanceService;

    public Void terminate(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        runtimeService.deleteProcessInstance(jobInstance.getRootProcInstId(), String.valueOf(JobInstanceStatus.TERMINATED.getStatus()));
        return null;
    }

    public Void pause(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        runtimeService.suspendProcessInstanceById(jobInstance.getRootProcInstId());
        return null;
    }

    public Void activate(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        runtimeService.activateProcessInstanceById(jobInstance.getRootProcInstId());
        return null;
    }

    private JobInstance getJobInstance(JobInstanceIdParam param) {
        return jobInstanceService.get(param.getId());
    }
}
