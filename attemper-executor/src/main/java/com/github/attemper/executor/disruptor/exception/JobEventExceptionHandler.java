package com.github.attemper.executor.disruptor.exception;

import com.github.attemper.executor.disruptor.event.JobEvent;
import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 */
@Slf4j
public class JobEventExceptionHandler implements ExceptionHandler<JobEvent> {
    @Override
    public void handleEventException(Throwable throwable, long l, JobEvent jobEvent) {
        log.error(jobEvent.getParam().getId(), throwable);
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
