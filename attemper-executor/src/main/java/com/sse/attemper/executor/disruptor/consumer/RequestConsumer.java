package com.sse.attemper.executor.disruptor.consumer;

import com.lmax.disruptor.WorkHandler;
import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.attemper.common.enums.JobInstanceStatus;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.monitor.JobExecution;
import com.sse.attemper.common.result.dispatch.monitor.JobInstance;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.executor.disruptor.container.RequestContainer;
import com.sse.attemper.executor.service.instance.JobExecutionOfExeService;
import com.sse.attemper.executor.service.instance.JobInstanceOfExeService;
import io.grpc.stub.StreamObserver;
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
        RuntimeService runtimeService = ContextBeanAware.getBean(RuntimeService.class);
        JobInvokingProto.JobInvokingRequest request = container.getJobInvokingRequest();
        JobInvokingProto.JobInvokingResponse.Builder responseBuilder = JobInvokingProto.JobInvokingResponse.newBuilder()
                .setId(request.getId());
        saveExecution(request);
        saveInstance(request);
        try {
            runtimeService.startProcessInstanceByKey(request.getJobName(), request.getId());
            responseBuilder.setCode(200).setMsg(CommonResult.ok().getMsg());
        } catch (Exception e) {
            if (StringUtils.isNotBlank(request.getTriggerName())) {
                handleResponse(responseBuilder, 2000, e.getMessage());
            } else {
                handleResponse(responseBuilder, 2001, e.getMessage());
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
        StreamObserver<JobInvokingProto.JobInvokingResponse> responseObserver = container.getResponseObserver();
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
        //responseObserver.onError(new RTException(500));
    }

    private void handleResponse(JobInvokingProto.JobInvokingResponse.Builder responseBuilder, int code, String msg) {
        responseBuilder.setCode(code).setMsg(msg);
    }

    private void saveExecution(JobInvokingProto.JobInvokingRequest request) {
        JobExecutionOfExeService jobExecutionOfExeService = ContextBeanAware.getBean(JobExecutionOfExeService.class);
        JobExecution jobExecution = JobExecution.builder()
                .id(request.getId())
                .jobName(request.getJobName())
                .triggerName(request.getTriggerName())
                .startTime(new Date())
                .tenantId(request.getTenantId())
                .build();
        jobExecution.setStatus(JobInstanceStatus.RUNNING.getStatus());
        jobExecutionOfExeService.add(jobExecution);
    }

    private void saveInstance(JobInvokingProto.JobInvokingRequest request) {
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        JobInstance jobInstance = JobInstance.builder()
                .id(request.getId())
                .triggerName(request.getTriggerName())
                .build();
        jobInstance.setStatus(JobInstanceStatus.RUNNING.getStatus());
        jobInstanceOfExeService.add(jobInstance);
    }
}
