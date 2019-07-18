package com.github.attemper.executor.camunda.incident;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.incident.DefaultIncidentHandler;
import org.camunda.bpm.engine.impl.incident.IncidentContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.Incident;

import java.util.Date;
import java.util.List;

@Slf4j
public class CustomJobIncidentHandler extends DefaultIncidentHandler {

    public CustomJobIncidentHandler(String type) {
        super(type);
    }

    @Override
    public Incident handleIncident(IncidentContext context, String message) {
        try {
            RuntimeService runtimeService = SpringContextAware.getBean(RuntimeService.class);
            JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
            List<Execution> executions = runtimeService.createExecutionQuery().executionId(context.getExecutionId()).list();
            for (Execution execution : executions) {
                ExecutionEntity executionEntity = (ExecutionEntity) execution;
                String id = executionEntity.getBusinessKey();
                updateInstance(jobInstanceService, id, 3052, executionEntity.getCurrentActivityId() == null ? executionEntity.getCurrentActivityName() : executionEntity.getCurrentActivityId());
            }
            updateInstanceAct(jobInstanceService, context.getExecutionId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return super.handleIncident(context, message);
    }

    private void updateInstance(JobInstanceService jobInstanceService, String id, int code, String msg) {
        JobInstance jobInstance = jobInstanceService.get(id);
        jobInstance
                .setEndTime(new Date())
                .setStatus(JobInstanceStatus.FAILURE.getStatus())
                .setCode(code);
        if (jobInstance.getMsg() == null) {
            jobInstance.setMsg(StatusProperty.getValue(code) + ":" + msg);
        } else {
            jobInstance.setMsg(jobInstance.getMsg() + "," + msg);
        }
        jobInstanceService.update(jobInstance);
    }

    private void updateInstanceAct(JobInstanceService jobInstanceService, String executionId) {
        JobInstanceAct jobInstanceAct = new JobInstanceAct()
                .setExecutionId(executionId)
                .setEndTime(new Date())
                .setStatus(JobInstanceStatus.FAILURE.getStatus());
        jobInstanceService.updateAct(jobInstanceAct);
    }
}
