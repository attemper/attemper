package com.github.attemper.executor.camunda.history.event.impl;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.param.dispatch.instance.InstanceGetParam;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.executor.camunda.history.event.EndEventing;
import com.github.attemper.executor.camunda.history.event.EventingAdapter;
import com.github.attemper.executor.camunda.history.event.StartEventing;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;

@Slf4j
public class HistoricProcessInstanceEventing extends EventingAdapter<HistoricProcessInstanceEventEntity> implements StartEventing<HistoricProcessInstanceEventEntity>, EndEventing<HistoricProcessInstanceEventEntity> {

    public HistoricProcessInstanceEventing(HistoricProcessInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
        Instance instance;
        if (historyEvent.getBusinessKey() != null) {
            instance = instanceService.get(new InstanceGetParam().setId(historyEvent.getBusinessKey()));
            instance.setProcInstId(historyEvent.getProcessInstanceId())
                    .setRootProcInstId(historyEvent.getRootProcessInstanceId())
                    .setSuperProcInstId(historyEvent.getSuperProcessInstanceId())
                    .setProcDefId(historyEvent.getProcessDefinitionId());
            instanceService.update(instance);
        } else {
            instance = new Instance()
                    .setId(SpringContextAware.getBean(IdGenerator.class).getNextId())
                    .setProcInstId(historyEvent.getProcessInstanceId())
                    .setRootProcInstId(historyEvent.getRootProcessInstanceId())
                    .setSuperProcInstId(historyEvent.getSuperProcessInstanceId())
                    .setProcDefId(historyEvent.getProcessDefinitionId())
                    .setStatus(InstanceStatus.RUNNING.getStatus())
                    .setJobName(historyEvent.getProcessDefinitionKey())
                    .setDisplayName(historyEvent.getProcessDefinitionName())
                    .setStartTime(historyEvent.getStartTime())
                    .setTenantId(historyEvent.getTenantId());
            Instance parentInstance = instanceService.get(new InstanceGetParam().setProcInstId(historyEvent.getRootProcessInstanceId()));
            if (parentInstance != null) {
                instance.setTriggerName(parentInstance.getTriggerName())
                        .setSchedulerUri(parentInstance.getSchedulerUri())
                        .setExecutorUri(parentInstance.getExecutorUri());
            }
            instanceService.add(instance);
        }
    }

    @Override
    public void end() {
        InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
        Instance instance = instanceService.get(new InstanceGetParam().setProcInstId(historyEvent.getProcessInstanceId()));
        instance.setStatus(InstanceStatus.SUCCESS.getStatus());
        instance.setEndTime(historyEvent.getEndTime());
        instance.setDuration(instance.getEndTime().getTime() - instance.getStartTime().getTime());
        instanceService.updateDone(instance);
    }
}
