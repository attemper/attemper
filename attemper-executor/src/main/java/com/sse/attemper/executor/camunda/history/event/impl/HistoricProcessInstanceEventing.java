package com.sse.attemper.executor.camunda.history.event.impl;

import com.sse.attemper.common.enums.JobInstanceStatus;
import com.sse.attemper.common.result.dispatch.monitor.JobExecution;
import com.sse.attemper.common.result.dispatch.monitor.JobExecutionAct;
import com.sse.attemper.common.result.dispatch.monitor.JobInstance;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.executor.camunda.history.event.EndEventing;
import com.sse.attemper.executor.camunda.history.event.EventingAdapter;
import com.sse.attemper.executor.camunda.history.event.StartEventing;
import com.sse.attemper.executor.service.instance.JobExecutionOfExeService;
import com.sse.attemper.executor.service.instance.JobInstanceOfExeService;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;

public class HistoricProcessInstanceEventing extends EventingAdapter<HistoricProcessInstanceEventEntity> implements StartEventing<HistoricProcessInstanceEventEntity>, EndEventing<HistoricProcessInstanceEventEntity> {

    public HistoricProcessInstanceEventing(HistoricProcessInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        JobExecution jobExecution = toJobExecution(historyEvent);
        jobExecution.setStatus(JobInstanceStatus.RUNNING.getStatus());
        JobExecutionOfExeService jobExecutionOfExeService = ContextBeanAware.getBean(JobExecutionOfExeService.class);
        jobExecutionOfExeService.update(jobExecution);

        JobInstance jobInstance = toJobInstance(historyEvent);
        jobInstance.setStatus(JobInstanceStatus.RUNNING.getStatus());
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        jobInstanceOfExeService.update(jobInstance);
    }

    @Override
    public void end() {
        JobExecutionOfExeService jobExecutionOfExeService = ContextBeanAware.getBean(JobExecutionOfExeService.class);
        JobExecution jobExecution = JobExecution.builder().id(historyEvent.getBusinessKey()).build();
        jobExecutionOfExeService.delete(jobExecution);
        JobExecutionAct jobExecutionAct = JobExecutionAct.builder()
                .rootProcInstId(historyEvent.getRootProcessInstanceId()).build();
        jobExecutionOfExeService.deleteAct(jobExecutionAct);

        JobInstance jobInstance = toJobInstance(historyEvent);
        jobInstance.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        jobInstanceOfExeService.update(jobInstance);
    }

    private JobExecution toJobExecution(HistoricProcessInstanceEventEntity historyEvent) {
        return JobExecution.builder()
                .id(historyEvent.getBusinessKey())
                .procInstId(historyEvent.getProcessInstanceId())
                .rootProcInstId(historyEvent.getRootProcessInstanceId())
                .procDefId(historyEvent.getProcessDefinitionId())
                .build();
    }

    private JobInstance toJobInstance(HistoricProcessInstanceEventEntity historyEvent) {
        return JobInstance.builder()
                .id(historyEvent.getBusinessKey())
                .procInstId(historyEvent.getProcessInstanceId())
                .build();
    }
}
