package com.github.attemper.executor.camunda.history.event.impl;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.camunda.history.event.EndEventing;
import com.github.attemper.executor.camunda.history.event.EventingAdapter;
import com.github.attemper.executor.camunda.history.event.StartEventing;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;

public class HistoricActivityInstanceEventing extends EventingAdapter<HistoricActivityInstanceEventEntity> implements StartEventing<HistoricActivityInstanceEventEntity>, EndEventing<HistoricActivityInstanceEventEntity> {
    public HistoricActivityInstanceEventing(HistoricActivityInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        JobInstanceAct jobInstanceAct = toJobInstanceAct(historyEvent);
        jobInstanceAct.setStatus(JobInstanceStatus.RUNNING.getStatus());
        jobInstanceAct.setStartTime(historyEvent.getStartTime());
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        jobInstanceService.addAct(jobInstanceAct);
    }

    @Override
    public void end() {
        JobInstanceAct jobInstanceAct = toJobInstanceAct(historyEvent);
        jobInstanceAct.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        jobInstanceAct.setEndTime(historyEvent.getEndTime());
        if (historyEvent.getEndTime() != null && historyEvent.getStartTime() != null) {
            jobInstanceAct.setDuration(historyEvent.getEndTime().getTime() - historyEvent.getStartTime().getTime());
        }
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        jobInstanceService.updateAct(jobInstanceAct);
    }

    private JobInstanceAct toJobInstanceAct(HistoricActivityInstanceEventEntity historyEvent) {
        return JobInstanceAct.builder()
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

}
