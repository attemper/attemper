package com.github.attemper.executor.camunda.history.event.impl;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.camunda.history.event.EndEventing;
import com.github.attemper.executor.camunda.history.event.EventingAdapter;
import com.github.attemper.executor.camunda.history.event.StartEventing;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;

@Slf4j
public class HistoricProcessInstanceEventing extends EventingAdapter<HistoricProcessInstanceEventEntity> implements StartEventing<HistoricProcessInstanceEventEntity>, EndEventing<HistoricProcessInstanceEventEntity> {

    public HistoricProcessInstanceEventing(HistoricProcessInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstance jobInstance = jobInstanceService.get(historyEvent.getBusinessKey());
        jobInstance.setProcInstId(historyEvent.getProcessInstanceId());
        jobInstance.setRootProcInstId(historyEvent.getRootProcessInstanceId());
        jobInstance.setProcDefId(historyEvent.getProcessDefinitionId());
        jobInstanceService.update(jobInstance);
    }

    @Override
    public void end() {
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstance jobInstance = jobInstanceService.get(historyEvent.getBusinessKey());
        jobInstance.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        jobInstance.setEndTime(historyEvent.getEndTime());
        jobInstance.setDuration(jobInstance.getEndTime().getTime() - jobInstance.getStartTime().getTime());
        jobInstanceService.update(jobInstance);
    }
}
