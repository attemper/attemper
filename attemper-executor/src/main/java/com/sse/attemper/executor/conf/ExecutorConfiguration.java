package com.sse.attemper.executor.conf;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.sse.attemper.executor.disruptor.consumer.RequestConsumer;
import com.sse.attemper.executor.disruptor.container.RequestContainer;
import com.sse.attemper.executor.disruptor.producer.RequestProducer;
import com.sse.attemper.executor.rpc.JobInvokingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

    @Value("${grpc.server.port}")
    private int rpcPort;

    @Value("${disruptor.bufferSize:1024}")
    private int bufferSize;

    @Bean
    public Server server() {
        return ServerBuilder.forPort(rpcPort)
                .addService(new JobInvokingServiceImpl())
                .build();
    }

    @Bean
    public RequestProducer producer() {
        // Construct the Disruptor
        Disruptor<RequestContainer> disruptor = new Disruptor<>(RequestContainer::new, bufferSize, DaemonThreadFactory.INSTANCE);
        // Connect the handler
        disruptor.handleEventsWithWorkerPool(new RequestConsumer());
        // Start the Disruptor, starts all threads running
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<RequestContainer> ringBuffer = disruptor.getRingBuffer();
        return new RequestProducer(ringBuffer);
    }

}
