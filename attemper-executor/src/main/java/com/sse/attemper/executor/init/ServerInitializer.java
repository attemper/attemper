package com.sse.attemper.executor.init;

import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ldang
 */
@Component
public class ServerInitializer {

    @PostConstruct
    public void initGrpcServer() throws Exception {
        Server server = ServerBuilder.forPort(5231)
                .addService(new JobInvokingServiceImpl()).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        //server.awaitTermination();
    }

    public static class JobInvokingServiceImpl extends JobInvokingServiceGrpc.JobInvokingServiceImplBase {

        @Override
        public void invoking(JobInvokingProto.JobInvokingRequest request, StreamObserver<JobInvokingProto.JobInvokingResponse> responseObserver) {
            System.out.println(request);

            String msg = "Job Name: " + request.getJobName();

            JobInvokingProto.JobInvokingResponse response = JobInvokingProto.JobInvokingResponse.newBuilder().setCode(200).setMsg(msg).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}


