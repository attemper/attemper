package com.sse.attemper.executor.disruptor.consumer;

import com.lmax.disruptor.WorkHandler;
import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.attemper.executor.disruptor.container.RequestContainer;

/**
 * consumer of request
 */
public class RequestConsumer implements WorkHandler<RequestContainer> {

    @Override
    public void onEvent(RequestContainer container) throws Exception {
        String msg = "Job Name: " + container.getJobInvokingRequest().getJobName();
        JobInvokingProto.JobInvokingResponse response = JobInvokingProto.JobInvokingResponse.newBuilder().setCode(200).setMsg(msg).build();
        container.getResponseObserver().onNext(response);
        container.getResponseObserver().onCompleted();
    }
}
