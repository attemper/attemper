package com.github.attemper.executor.disruptor.producer;

import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.executor.JobInvokingResult;
import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

/**
 * producer of request
 */
public class RequestProducer {

    private RingBuffer<RequestContainer> ringBuffer;

    public RequestProducer(RingBuffer<RequestContainer> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorTwoArg<RequestContainer, JobInvokingParam, CommonResult<JobInvokingResult>> TRANSLATOR =
            (event, sequence, param, result) -> {
                event.setParam(param);
                event.setResult(result);
            };

    public void onData(JobInvokingParam param, CommonResult<JobInvokingResult> result) {
        ringBuffer.publishEvent(TRANSLATOR, param, result);
    }

}
