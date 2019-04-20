package com.sse.attemper.executor.camunda.history;

import com.sse.attemper.executor.camunda.history.event.impl.HistoricActivityInstanceEventing;
import com.sse.attemper.executor.camunda.history.event.impl.HistoricProcessInstanceEventing;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;

import java.util.List;

public class CustomHistoryEventHandler implements HistoryEventHandler {

    @Override
    public void handleEvent(HistoryEvent historyEvent) {
        handleEventIntern(historyEvent);
    }

    @Override
    public void handleEvents(List<HistoryEvent> historyEvents) {
        if (historyEvents != null && historyEvents.size() > 0) {
            for (HistoryEvent historyEvent : historyEvents) {
                handleEvent(historyEvent);
            }
        }
    }

    private void handleEventIntern(HistoryEvent historyEvent) {
        if (historyEvent instanceof HistoricProcessInstanceEventEntity) {
            HistoricProcessInstanceEventing eventing = new HistoricProcessInstanceEventing((HistoricProcessInstanceEventEntity) historyEvent);
            if (HistoryEventTypes.PROCESS_INSTANCE_START.getEventName().equals(historyEvent.getEventType())) {
                eventing.start();
            } else if (HistoryEventTypes.PROCESS_INSTANCE_END.getEventName().equals(historyEvent.getEventType())) {
                eventing.end();
            }
        } else if (historyEvent instanceof HistoricActivityInstanceEventEntity) {
            HistoricActivityInstanceEventing eventing = new HistoricActivityInstanceEventing((HistoricActivityInstanceEventEntity) historyEvent);
            if (HistoryEventTypes.ACTIVITY_INSTANCE_START.getEventName().equals(historyEvent.getEventType())) {
                eventing.start();
            } else if (HistoryEventTypes.ACTIVITY_INSTANCE_END.getEventName().equals(historyEvent.getEventType())) {
                eventing.end();
            }
        }
    }
}
