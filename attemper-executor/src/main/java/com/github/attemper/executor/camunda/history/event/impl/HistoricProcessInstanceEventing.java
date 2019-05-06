package com.github.attemper.executor.camunda.history.event.impl;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.dispatch.monitor.JobInstance;
import com.github.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.github.attemper.config.base.bean.ContextBeanAware;
import com.github.attemper.executor.camunda.history.event.EndEventing;
import com.github.attemper.executor.camunda.history.event.EventingAdapter;
import com.github.attemper.executor.camunda.history.event.StartEventing;
import com.github.attemper.executor.service.instance.JobExecutionOfExeService;
import com.github.attemper.executor.service.instance.JobInstanceOfExeService;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;

public class HistoricProcessInstanceEventing extends EventingAdapter<HistoricProcessInstanceEventEntity> implements StartEventing<HistoricProcessInstanceEventEntity>, EndEventing<HistoricProcessInstanceEventEntity> {

    public HistoricProcessInstanceEventing(HistoricProcessInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        JobInstance jobInstance = toJobInstance(historyEvent);
        jobInstance.setStatus(JobInstanceStatus.RUNNING.getStatus());
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        jobInstanceOfExeService.update(jobInstance);
    }

    @Override
    public void end() {
        JobInstance jobInstance = toJobInstance(historyEvent);
        jobInstance.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        jobInstanceOfExeService.update(jobInstance);

        JobExecutionOfExeService jobExecutionOfExeService = ContextBeanAware.getBean(JobExecutionOfExeService.class);
        jobExecutionOfExeService.delete(jobInstance);
        JobInstanceAct jobInstanceAct = JobInstanceAct.builder()
                .rootProcInstId(historyEvent.getRootProcessInstanceId()).build();
        jobExecutionOfExeService.deleteAct(jobInstanceAct);
    }

    private JobInstance toJobInstance(HistoricProcessInstanceEventEntity historyEvent) {
        return JobInstance.builder()
                .id(historyEvent.getBusinessKey())
                .procInstId(historyEvent.getProcessInstanceId())
                .rootProcInstId(historyEvent.getRootProcessInstanceId())
                .procDefId(historyEvent.getProcessDefinitionId())
                .endTime(historyEvent.getEndTime())
                .duration(historyEvent.getDurationInMillis())
                .build();
    }
}
