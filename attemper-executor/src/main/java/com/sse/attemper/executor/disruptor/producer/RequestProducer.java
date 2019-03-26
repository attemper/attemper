package com.sse.attemper.executor.disruptor.producer;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingRequest;
import com.sse.attemper.executor.disruptor.container.RequestContainer;
import io.grpc.stub.StreamObserver;

/**
 * producer of request
 */
public class RequestProducer {

    private RingBuffer<RequestContainer> ringBuffer;

    public RequestProducer(RingBuffer<RequestContainer> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorTwoArg<RequestContainer, JobInvokingRequest, StreamObserver<JobInvokingProto.JobInvokingResponse>> TRANSLATOR =
            (event, sequence, request, responseObserver) -> {
                event.setJobInvokingRequest(request);
                event.setResponseObserver(responseObserver);
            };

    public void onData(JobInvokingRequest jobInvokingRequest, StreamObserver<JobInvokingProto.JobInvokingResponse> responseObserver) {
        ringBuffer.publishEvent(TRANSLATOR, jobInvokingRequest, responseObserver);
    }

}
