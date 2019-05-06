package com.github.attemper.executor.rpc;

import com.github.atemper.grpc.invoking.JobInvokingProto.JobInvokingRequest;
import com.github.atemper.grpc.invoking.JobInvokingProto.JobInvokingResponse;
import com.github.atemper.grpc.invoking.JobInvokingServiceGrpc.JobInvokingServiceImplBase;
import com.github.attemper.config.base.bean.ContextBeanAware;
import com.github.attemper.executor.disruptor.producer.RequestProducer;
import io.grpc.stub.StreamObserver;

public class JobInvokingServiceImpl extends JobInvokingServiceImplBase {
    @Override
    public void invoke(JobInvokingRequest request, StreamObserver<JobInvokingResponse> responseObserver) {
        RequestProducer producer = ContextBeanAware.getBean(RequestProducer.class);
        producer.onData(request, responseObserver);
    }
}