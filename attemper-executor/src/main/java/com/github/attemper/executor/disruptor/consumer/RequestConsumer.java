package com.github.attemper.executor.disruptor.consumer;

import com.github.atemper.grpc.invoking.JobInvokingProto;
import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.monitor.JobInstance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.github.attemper.executor.service.instance.JobInstanceOfExeService;
import com.lmax.disruptor.WorkHandler;
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
        RuntimeService runtimeService = SpringContextAware.getBean(RuntimeService.class);
        JobInvokingProto.JobInvokingRequest request = container.getJobInvokingRequest();
        JobInvokingProto.JobInvokingResponse.Builder responseBuilder = JobInvokingProto.JobInvokingResponse.newBuilder()
                .setId(request.getId());
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

    private void saveInstance(JobInvokingProto.JobInvokingRequest request) {
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
