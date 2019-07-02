package com.github.attemper.executor.disruptor.producer;

import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.executor.disruptor.event.JobEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * producer of request
 */
public class RequestProducer {

    private RingBuffer<JobEvent> ringBuffer;

    public RequestProducer(RingBuffer<JobEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<JobEvent, JobInvokingParam> TRANSLATOR =
            (event, sequence, param) -> {
                event.setParam(param);
            };

    public void onData(JobInvokingParam param) {
        ringBuffer.publishEvent(TRANSLATOR, param);
    }

}
