package com.github.attemper.executor.camunda.history.event;

import org.camunda.bpm.engine.impl.history.event.HistoryEvent;

public abstract class EventingAdapter<T extends HistoryEvent> {

    protected T historyEvent;

    public EventingAdapter() {

    }

    public EventingAdapter(T historyEvent) {
        this.historyEvent = historyEvent;
    }
}
