package com.github.attemper.executor.camunda.incident;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.param.dispatch.instance.JobInstanceGetParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.camunda.cmd.UpdateHistoricInstanceCmd;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.RuntimeServiceImpl;
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
            RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) SpringContextAware.getBean(RuntimeService.class);
            JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
            List<Execution> executions = runtimeService.createExecutionQuery().executionId(context.getExecutionId()).list();
            for (Execution execution : executions) {
                ExecutionEntity executionEntity = (ExecutionEntity) execution;
                updateInstance(runtimeService, jobInstanceService, executionEntity, 3052);
                // make super fail
                ExecutionEntity superExecution = executionEntity.getSuperExecution();
                while (superExecution != null) {
                    updateInstance(runtimeService, jobInstanceService, superExecution, 3052);
                    superExecution = superExecution.getSuperExecution();
                }
            }
            updateInstanceAct(jobInstanceService, context.getExecutionId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return super.handleIncident(context, message);
    }

    private void updateInstance(RuntimeServiceImpl runtimeService, JobInstanceService jobInstanceService, ExecutionEntity execution, int code) {
        JobInstance jobInstance = jobInstanceService.get(new JobInstanceGetParam().setProcInstId(execution.getProcessInstanceId()));
        Date endTime = new Date();
        long duration = 0;
        if (jobInstance != null) {
            duration = endTime.getTime() - jobInstance.getStartTime().getTime();
            jobInstance
                    .setEndTime(endTime)
                    .setDuration(duration)
                    .setStatus(JobInstanceStatus.FAILURE.getStatus())
                    .setCode(code);
            String message = execution.getCurrentActivityId() == null ?
                    execution.getCurrentActivityName() : execution.getCurrentActivityId();
            if (jobInstance.getMsg() == null) {
                jobInstance.setMsg(StatusProperty.getValue(code) + ":" + message);
            } else {
                jobInstance.setMsg(jobInstance.getMsg() + "," + message);
            }
            jobInstanceService.updateDone(jobInstance);
        }
        runtimeService.getCommandExecutor().execute(new UpdateHistoricInstanceCmd(
                execution.getProcessInstanceId(),
                endTime,
                duration,
                HistoricProcessInstance.STATE_EXTERNALLY_TERMINATED));
    }

    private void updateInstanceAct(JobInstanceService jobInstanceService, String executionId) {
        JobInstanceAct jobInstanceAct = new JobInstanceAct()
                .setExecutionId(executionId)
                .setEndTime(new Date())
                .setStatus(JobInstanceStatus.FAILURE.getStatus());
        jobInstanceService.updateAct(jobInstanceAct);
    }
}
