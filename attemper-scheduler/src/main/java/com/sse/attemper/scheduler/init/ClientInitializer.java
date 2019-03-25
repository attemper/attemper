package com.sse.attemper.scheduler.init;

import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc;
import com.sse.attemper.scheduler.autoconfigure.DiscoveryClientChannelFactory;
import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ldang
 */
@Component
public class ClientInitializer {

    @Autowired
    private DiscoveryClientChannelFactory channelFactory;

    @PostConstruct
    public void initGrpcClient() throws Exception {
        Channel channel = channelFactory.create("executor");

        JobInvokingServiceGrpc.JobInvokingServiceBlockingStub stub =
                JobInvokingServiceGrpc.newBlockingStub(channel);
        JobInvokingProto.JobInvokingResponse helloResponse = stub.invoking(
                JobInvokingProto.JobInvokingRequest.newBuilder()
                        .setSequenceNo("1")
                        .setJobName("FLOW1")
                        .build());
        System.out.println(helloResponse);

        //((ManagedChannel) channel).shutdown();
    }
}


