package com.github.attemper.executor.conf;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.executor.camunda.history.event.impl.HistoricActivityInstanceEventing;
import com.github.attemper.executor.camunda.history.event.impl.HistoricProcessInstanceEventing;
import com.github.attemper.executor.util.CamundaUtil;
import jsr223.shell.ShellHandler;
import org.camunda.bpm.engine.ActivityTypes;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.core.model.PropertyKey;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringEventBusListener {

    private static final PropertyKey PROPERTY_KEY = new PropertyKey<>(CommonConstants.type);

    @EventListener
    public void onExecutionEvent(DelegateExecution executionDelegate) {
        if (executionDelegate.getActivityInstanceId() != null && executionDelegate instanceof ExecutionEntity) {
            ExecutionEntity executionEntity = (ExecutionEntity) executionDelegate;
            if (ActivityTypes.TASK_SCRIPT.equals(executionEntity.getActivity().getProperties().get(PROPERTY_KEY))) {
                String key = ShellHandler.UNIQUE_SEQUENCE_NO + executionDelegate.getActivityInstanceId();
                if (HistoryEventTypes.ACTIVITY_INSTANCE_START.getEventName().equals(executionDelegate.getEventName())) {
                    executionDelegate.setVariable(key, CommonConstants.EMPTY);
                } else if (HistoryEventTypes.ACTIVITY_INSTANCE_END.getEventName().equals(executionDelegate.getEventName())) {
                    String result = System.getProperty(key);
                    System.clearProperty(key);
                    if (result != null) {
                        InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
                        InstanceAct instanceAct = new InstanceAct()
                                .setId(CamundaUtil.extractIdFromActInstanceId(executionDelegate.getActivityInstanceId()))
                                .setLogText(result);
                        instanceService.updateAct(instanceAct);
                    }
                }
            }
        }
    }


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
