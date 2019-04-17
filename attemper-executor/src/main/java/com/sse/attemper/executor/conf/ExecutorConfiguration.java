package com.sse.attemper.executor.conf;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.sse.attemper.executor.disruptor.consumer.RequestConsumer;
import com.sse.attemper.executor.disruptor.container.RequestContainer;
import com.sse.attemper.executor.disruptor.exception.RequestContainerExceptionHandler;
import com.sse.attemper.executor.disruptor.producer.RequestProducer;
import com.sse.attemper.executor.interceptor.HeaderServerInterceptor;
import com.sse.attemper.executor.rpc.JobInvokingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

    @Value("${grpc.server.port}")
    private int rpcPort;

    @Bean
    public Server server() {
        return ServerBuilder.forPort(rpcPort)
                .addService(ServerInterceptors.intercept(new JobInvokingServiceImpl(), new HeaderServerInterceptor()))
                .build();
    }

    @Value("${disruptor.buffer-size:1024}")
    private int bufferSize;

    @Bean
    public RequestProducer producer() {
        // Construct the Disruptor
        Disruptor<RequestContainer> disruptor = new Disruptor<>(RequestContainer::new, bufferSize, DaemonThreadFactory.INSTANCE);
        // Connect the handler
        disruptor.handleEventsWithWorkerPool(new RequestConsumer()).then(new EventHandler<RequestContainer>() {
            @Override
            public void onEvent(RequestContainer requestContainer, long l, boolean b) throws Exception {
                // Failing to call clear here will result in the
                // object associated with the event to live until
                // it is overwritten once the ring buffer has wrapped
                // around to the beginning.
                requestContainer.clear();
            }
        });
        disruptor.setDefaultExceptionHandler(new RequestContainerExceptionHandler());
        // Start the Disruptor, starts all threads running
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<RequestContainer> ringBuffer = disruptor.getRingBuffer();
        return new RequestProducer(ringBuffer);
    }

}
