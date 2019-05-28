package com.github.attemper.executor.disruptor.consumer;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.monitor.JobInstance;
import com.github.attemper.common.result.executor.JobInvokingResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.github.attemper.executor.service.instance.JobInstanceOfExeService;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RuntimeService;

import java.util.Date;

/**
 * consumer of request
 */
@Slf4j
public class RequestConsumer implements WorkHandler<RequestContainer> {

    @Override
    public void onEvent(RequestContainer container) throws Exception {
        RuntimeService runtimeService = SpringContextAware.getBean(RuntimeService.class);
        JobInvokingParam request = container.getParam();
        JobInvokingResult responseBuilder = new JobInvokingResult(request.getId());
        CommonResult<JobInvokingResult> commonResult;
        saveInstance(request);
        try {
            runtimeService.startProcessInstanceByKey(request.getJobName(), request.getId());
            commonResult = CommonResult.ok();
        } catch (Exception e) {
            if (StringUtils.isNotBlank(request.getTriggerName())) {
                commonResult = CommonResult.put(2000, e.getMessage());
            } else {
                commonResult = CommonResult.put(2001, e.getMessage());
            }
            /*jobExecution.setStatus(JobInstanceStatus.FAILURE.getStatus());
            jobExecutionOfExeService.update(jobExecution);
            JobInstance jobInstance = JobInstance.builder()
                    .id(request.getId())
                    .build();
            jobInstance.setStatus(JobInstanceStatus.FAILURE.getStatus());
            jobInstance.setCode(responseBuilder.getCode());
            jobExecutionOfExeService.addDetail(jobInstance);*/
        }
        commonResult.setResult(responseBuilder);
        container.setResult(commonResult);
    }

    private void saveInstance(JobInvokingParam request) {
        JobInstanceOfExeService jobInstanceOfExeService = SpringContextAware.getBean(JobInstanceOfExeService.class);
        JobInstance jobInstance = JobInstance.builder()
                .id(request.getId())
                .jobName(request.getJobName())
                .triggerName(request.getTriggerName())
                .startTime(new Date())
                .tenantId(request.getTenantId())
                .build();
        jobInstance.setStatus(JobInstanceStatus.RUNNING.getStatus());
        jobInstanceOfExeService.add(jobInstance);
    }
}
