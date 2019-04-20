package com.sse.attemper.executor.camunda.history.event.impl;

import com.sse.attemper.common.enums.JobInstanceStatus;
import com.sse.attemper.common.result.dispatch.monitor.JobExecutionAct;
import com.sse.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.executor.camunda.history.event.EndEventing;
import com.sse.attemper.executor.camunda.history.event.EventingAdapter;
import com.sse.attemper.executor.camunda.history.event.StartEventing;
import com.sse.attemper.executor.service.instance.JobExecutionOfExeService;
import com.sse.attemper.executor.service.instance.JobInstanceOfExeService;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;

public class HistoricActivityInstanceEventing extends EventingAdapter<HistoricActivityInstanceEventEntity> implements StartEventing<HistoricActivityInstanceEventEntity>, EndEventing<HistoricActivityInstanceEventEntity> {
    public HistoricActivityInstanceEventing(HistoricActivityInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        JobExecutionAct jobExecutionAct = toJobExecutionAct(historyEvent);
        jobExecutionAct.setStatus(JobInstanceStatus.RUNNING.getStatus());
        jobExecutionAct.setStartTime(historyEvent.getStartTime());
        JobExecutionOfExeService jobExecutionOfExeService = ContextBeanAware.getBean(JobExecutionOfExeService.class);
        jobExecutionOfExeService.addAct(jobExecutionAct);

        JobInstanceAct jobInstanceAct = toJobInstanceAct(historyEvent);
        jobInstanceAct.setStatus(JobInstanceStatus.RUNNING.getStatus());
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        jobInstanceOfExeService.addAct(jobInstanceAct);
    }

    @Override
    public void end() {
        JobExecutionAct jobExecutionAct = toJobExecutionAct(historyEvent);
        jobExecutionAct.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        jobExecutionAct.setEndTime(historyEvent.getEndTime());
        jobExecutionAct.setDuration(historyEvent.getDurationInMillis());
        JobExecutionOfExeService jobExecutionOfExeService = ContextBeanAware.getBean(JobExecutionOfExeService.class);
        jobExecutionOfExeService.updateAct(jobExecutionAct);

        JobInstanceAct jobInstanceAct = toJobInstanceAct(historyEvent);
        jobInstanceAct.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        JobInstanceOfExeService jobInstanceOfExeService = ContextBeanAware.getBean(JobInstanceOfExeService.class);
        jobInstanceOfExeService.updateAct(jobInstanceAct);
    }

    private JobExecutionAct toJobExecutionAct(HistoricActivityInstanceEventEntity historyEvent) {
        return JobExecutionAct.builder()
                .id(historyEvent.getId().substring(historyEvent.getActivityId().length() + 1))
                .actInstId(historyEvent.getActivityInstanceId())
                .parentActInstId(historyEvent.getParentActivityInstanceId())
                .executionId(historyEvent.getExecutionId())
                .procInstId(historyEvent.getProcessInstanceId())
                .rootProcInstId(historyEvent.getRootProcessInstanceId())
                .actId(historyEvent.getActivityId())
                .actName(historyEvent.getActivityName())
                .actType(historyEvent.getActivityType())
                .build();
    }

    private JobInstanceAct toJobInstanceAct(HistoricActivityInstanceEventEntity historyEvent) {
        return JobInstanceAct.builder()
                .id(historyEvent.getId().substring(historyEvent.getActivityId().length() + 1))
                .actInstId(historyEvent.getActivityInstanceId())
                .build();
    }
}
