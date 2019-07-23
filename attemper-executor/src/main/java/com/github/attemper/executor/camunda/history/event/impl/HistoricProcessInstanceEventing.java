package com.github.attemper.executor.camunda.history.event.impl;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.param.dispatch.instance.JobInstanceGetParam;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.camunda.history.event.EndEventing;
import com.github.attemper.executor.camunda.history.event.EventingAdapter;
import com.github.attemper.executor.camunda.history.event.StartEventing;
import com.github.attemper.executor.util.CamundaUtil;
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
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstance jobInstance;
        if (historyEvent.getBusinessKey() != null) {
            jobInstance = jobInstanceService.get(new JobInstanceGetParam().setId(historyEvent.getBusinessKey()));
            jobInstance.setProcInstId(historyEvent.getProcessInstanceId())
                    .setRootProcInstId(historyEvent.getRootProcessInstanceId())
                    .setProcDefId(historyEvent.getProcessDefinitionId());
            jobInstanceService.update(jobInstance);
        } else {
            jobInstance = new JobInstance()
                    .setId(SpringContextAware.getBean(IdGenerator.class).getNextId())
                    .setProcInstId(historyEvent.getProcessInstanceId())
                    .setRootProcInstId(historyEvent.getRootProcessInstanceId())
                    .setProcDefId(historyEvent.getProcessDefinitionId())
                    .setStatus(JobInstanceStatus.RUNNING.getStatus())
                    .setJobName(CamundaUtil.extractKeyFromProcessDefinitionId(historyEvent.getProcessDefinitionId()))
                    .setStartTime(historyEvent.getStartTime())
                    .setTenantId(historyEvent.getTenantId());
            JobInstance parentJobInstance = jobInstanceService.get(new JobInstanceGetParam().setProcInstId(historyEvent.getRootProcessInstanceId()));
            if (parentJobInstance != null) {
                jobInstance.setTriggerName(parentJobInstance.getTriggerName())
                        .setSchedulerUri(parentJobInstance.getSchedulerUri())
                        .setExecutorUri(parentJobInstance.getExecutorUri());
            }
            jobInstanceService.add(jobInstance);
        }
    }

    @Override
    public void end() {
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstance jobInstance = jobInstanceService.get(new JobInstanceGetParam().setProcInstId(historyEvent.getProcessInstanceId()));
        jobInstance.setStatus(JobInstanceStatus.SUCCESS.getStatus());
        jobInstance.setEndTime(historyEvent.getEndTime());
        jobInstance.setDuration(jobInstance.getEndTime().getTime() - jobInstance.getStartTime().getTime());
        jobInstanceService.update(jobInstance);
    }
}
