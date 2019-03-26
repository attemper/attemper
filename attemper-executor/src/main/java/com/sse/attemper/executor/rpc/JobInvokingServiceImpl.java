package com.sse.attemper.executor.rpc;

import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingRequest;
import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingResponse;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc.JobInvokingServiceImplBase;
import io.grpc.stub.StreamObserver;

public class JobInvokingServiceImpl extends JobInvokingServiceImplBase {
    @Override
    public void invoke(JobInvokingRequest request, StreamObserver<JobInvokingResponse> responseObserver) {
        System.out.println(request);

        String msg = "Job Name: " + request.getJobName();

        JobInvokingResponse response = JobInvokingResponse.newBuilder().setCode(200).setMsg(msg).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}