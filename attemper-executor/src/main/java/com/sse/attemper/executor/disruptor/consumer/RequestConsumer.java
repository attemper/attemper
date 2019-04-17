package com.sse.attemper.executor.disruptor.consumer;

import com.lmax.disruptor.WorkHandler;
import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.executor.disruptor.container.RequestContainer;
import io.grpc.stub.StreamObserver;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * consumer of request
 */
public class RequestConsumer implements WorkHandler<RequestContainer> {

    @Override
    public void onEvent(RequestContainer container) throws Exception {
        RuntimeService runtimeService = ContextBeanAware.getBean(RuntimeService.class);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(container.getJobInvokingRequest().getJobName());
        String msg = "JobName[" + container.getJobInvokingRequest().getJobName() + "] start success:instanceId=" + processInstance.getProcessInstanceId();
        JobInvokingProto.JobInvokingResponse response = JobInvokingProto.JobInvokingResponse.newBuilder().setCode(200).setMsg(msg).build();
        StreamObserver<JobInvokingProto.JobInvokingResponse> responseObserver = container.getResponseObserver();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        //responseObserver.onError(new RTException(500));
    }
}
