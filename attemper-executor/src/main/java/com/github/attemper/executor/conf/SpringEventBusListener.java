package com.github.attemper.executor.conf;

import com.github.attemper.executor.camunda.history.event.impl.HistoricActivityInstanceEventing;
import com.github.attemper.executor.camunda.history.event.impl.HistoricProcessInstanceEventing;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringEventBusListener {
    /*
    @EventListener
    public void onTaskEvent(DelegateTask taskDelegate) {}
    */

    /*
    @EventListener
    public void onExecutionEvent(DelegateExecution executionDelegate) {}
    */

    @EventListener
    public void onHistoryEvent(HistoryEvent historyEvent) {
        handleEventIntern(historyEvent);
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
