package com.github.attemper.executor.conf;

import com.github.attemper.executor.disruptor.consumer.RequestConsumer;
import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.github.attemper.executor.disruptor.exception.RequestContainerExceptionHandler;
import com.github.attemper.executor.disruptor.producer.RequestProducer;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

    @Value("${disruptor.buffer-size:1024}")
    private int bufferSize;

    @Value("${disruptor.consumer-num:128}")
    private int consumerNum;

    @Bean
    public RequestProducer producer() {
        // Construct the Disruptor
        Disruptor<RequestContainer> disruptor = new Disruptor<>(RequestContainer::new, bufferSize, DaemonThreadFactory.INSTANCE);
        //create consumers by length of consumerNum
        RequestConsumer[] consumers = new RequestConsumer[consumerNum];
        for (int i = 0; i < consumerNum; i++) {
            consumers[i] = new RequestConsumer();
        }
        // Connect the handler
        disruptor.handleEventsWithWorkerPool(consumers).then((EventHandler<RequestContainer>) (requestContainer, l, b) -> {
            // Failing to call clear here will result in the
            // object associated with the event to live until
            // it is overwritten once the ring buffer has wrapped
            // around to the beginning.
            requestContainer.clear();
        });
        disruptor.setDefaultExceptionHandler(new RequestContainerExceptionHandler());
        // Start the Disruptor, starts all threads running
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<RequestContainer> ringBuffer = disruptor.getRingBuffer();
        return new RequestProducer(ringBuffer);
    }
}
