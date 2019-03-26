package com.sse.attemper.executor.rpc;

import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingRequest;
import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingResponse;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc.JobInvokingServiceImplBase;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.executor.disruptor.producer.RequestProducer;
import io.grpc.stub.StreamObserver;

public class JobInvokingServiceImpl extends JobInvokingServiceImplBase {
    @Override
    public void invoke(JobInvokingRequest request, StreamObserver<JobInvokingResponse> responseObserver) {
        RequestProducer producer = ContextBeanAware.getBean(RequestProducer.class);
        producer.onData(request, responseObserver);
    }
}