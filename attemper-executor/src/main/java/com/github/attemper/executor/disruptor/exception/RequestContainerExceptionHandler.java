package com.github.attemper.executor.disruptor.exception;

import com.github.attemper.executor.disruptor.container.RequestContainer;
import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 */
@Slf4j
public class RequestContainerExceptionHandler implements ExceptionHandler<RequestContainer> {
    @Override
    public void handleEventException(Throwable throwable, long l, RequestContainer requestContainer) {
        log.error(requestContainer.getParam().getId(), throwable);
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
