package com.sse.attemper.executor.disruptor.container;

import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingRequest;
import io.grpc.stub.StreamObserver;

public class RequestContainer {

    private JobInvokingRequest jobInvokingRequest;

    private StreamObserver<JobInvokingProto.JobInvokingResponse> responseObserver;

    public RequestContainer() {
    }

    public JobInvokingRequest getJobInvokingRequest() {
        return jobInvokingRequest;
    }

    public void setJobInvokingRequest(JobInvokingRequest jobInvokingRequest) {
        this.jobInvokingRequest = jobInvokingRequest;
    }

    public StreamObserver<JobInvokingProto.JobInvokingResponse> getResponseObserver() {
        return responseObserver;
    }

    public void setResponseObserver(StreamObserver<JobInvokingProto.JobInvokingResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    public void clear() {
        jobInvokingRequest = null;
        responseObserver = null;
    }
}
