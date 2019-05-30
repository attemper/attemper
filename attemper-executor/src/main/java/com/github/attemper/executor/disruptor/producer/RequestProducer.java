package com.github.attemper.executor.disruptor.producer;

import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * producer of request
 */
public class RequestProducer {

    private RingBuffer<RequestContainer> ringBuffer;

    public RequestProducer(RingBuffer<RequestContainer> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<RequestContainer, JobInvokingParam> TRANSLATOR =
            (event, sequence, param) -> {
                event.setParam(param);
            };

    public void onData(JobInvokingParam param) {
        ringBuffer.publishEvent(TRANSLATOR, param);
    }

}
