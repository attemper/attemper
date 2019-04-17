package com.sse.attemper.executor.disruptor.exception;

import com.lmax.disruptor.ExceptionHandler;
import com.sse.attemper.executor.disruptor.container.RequestContainer;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 */
@Slf4j
public class RequestContainerExceptionHandler implements ExceptionHandler<RequestContainer> {
    @Override
    public void handleEventException(Throwable throwable, long l, RequestContainer requestContainer) {
        log.error(requestContainer.getJobInvokingRequest().getId(), throwable);
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }
}
