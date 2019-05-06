package com.github.attemper.executor.disruptor.container;

import com.lmax.disruptor.EventFactory;

public class RequestContainerFactory implements EventFactory<RequestContainer> {
    @Override
    public RequestContainer newInstance() {
        return new RequestContainer();
    }
}
