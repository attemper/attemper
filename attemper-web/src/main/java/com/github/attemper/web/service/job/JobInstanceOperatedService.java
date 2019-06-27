package com.github.attemper.web.service.job;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.JobInstanceIdParam;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.web.service.ExecutorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class JobInstanceOperatedService {

    @Autowired
    private JobInstanceService jobInstanceService;

    @Autowired
    private JobCallingService jobCallingService;

    @Autowired
    private ExecutorHandler executorHandler;

    @Autowired
    private IdGenerator idGenerator;

    public Void retry(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        if(!isDone(jobInstance.getStatus())) {
            throw new RTException(6202, String.valueOf(jobInstance.getStatus()));
        }
        String parentId;
        if (StringUtils.isNotBlank(jobInstance.getParentId())) {
            parentId = jobInstance.getParentId();
            JobInstance rootRetriedJobInstance = jobInstanceService.get(parentId);
            if (rootRetriedJobInstance != null && !rootRetriedJobInstance.isRetried()) {
                rootRetriedJobInstance.setRetried(true);
                jobInstanceService.update(rootRetriedJobInstance);
            } else {
                log.error("{} is non-existent", jobInstance.getParentId());
            }
        } else {
            parentId = jobInstance.getId();
            if (!jobInstance.isRetried()) {
                jobInstance.setRetried(true);
                jobInstanceService.update(jobInstance);
            }
        }
        jobCallingService.retry(idGenerator.getNextId(), jobInstance.getJobName(), jobInstance.getTenantId(), parentId);
        return null;
    }

    public Void terminate(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        if (!isDoing(jobInstance.getStatus())) {
            throw new RTException(6202, String.valueOf(jobInstance.getStatus()));
        }
        executorHandler.terminate(jobInstance.getExecutorUri(), jobInstance);
        updateJobInstanceStatus(jobInstance, JobInstanceStatus.TERMINATED.getStatus());
        return null;
    }

    public Void pause(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        if (jobInstance.getStatus() != JobInstanceStatus.RUNNING.getStatus()) {
            throw new RTException(6202, String.valueOf(jobInstance.getStatus()));
        }
        executorHandler.pause(jobInstance.getExecutorUri(), jobInstance);
        updateJobInstanceStatus(jobInstance, JobInstanceStatus.PAUSED.getStatus());
        return null;
    }

    public Void activate(JobInstanceIdParam param) {
        JobInstance jobInstance = getJobInstance(param);
        if (jobInstance.getStatus() != JobInstanceStatus.PAUSED.getStatus()) {
            throw new RTException(6202, String.valueOf(jobInstance.getStatus()));
        }
        executorHandler.activate(jobInstance.getExecutorUri(), jobInstance);
        updateJobInstanceStatus(jobInstance, JobInstanceStatus.RUNNING.getStatus());
        return null;
    }

    private void updateJobInstanceStatus(JobInstance jobInstance, int status) {
        jobInstance.setStatus(status);
        jobInstanceService.update(jobInstance);
    }

    private boolean isDoing(int status) {
        return status == JobInstanceStatus.RUNNING.getStatus() ||
                status == JobInstanceStatus.PAUSED.getStatus();
    }

    private boolean isDone(int status) {
        return status == JobInstanceStatus.SUCCESS.getStatus() ||
                status == JobInstanceStatus.FAILURE.getStatus() ||
                status == JobInstanceStatus.TERMINATED.getStatus();
    }

    private JobInstance getJobInstance(JobInstanceIdParam param) {
        JobInstance jobInstance = jobInstanceService.get(param.getId());
        if (jobInstance == null) {
            throw new RTException(6201);
        }
        return jobInstance;
    }
}
