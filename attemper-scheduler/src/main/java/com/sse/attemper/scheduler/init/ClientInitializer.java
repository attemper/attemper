package com.sse.attemper.scheduler.init;

import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ldang
 */
@Component
public class ClientInitializer {

    @PostConstruct
    public void initGrpcClient() throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 5230)
                .usePlaintext(true)
                .build();

        JobInvokingServiceGrpc.JobInvokingServiceBlockingStub stub =
                JobInvokingServiceGrpc.newBlockingStub(channel);

        JobInvokingProto.JobInvokingResponse helloResponse = stub.invoking(
                JobInvokingProto.JobInvokingRequest.newBuilder()
                        .setSequenceNo("1")
                        .setJobName("FLOW1")
                        .build());

        System.out.println(helloResponse);

        channel.shutdown();
    }
}


