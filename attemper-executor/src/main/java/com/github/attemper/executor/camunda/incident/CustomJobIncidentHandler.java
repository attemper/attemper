package com.github.attemper.executor.camunda.incident;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.InstanceService;
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
            InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
            List<Execution> executions = runtimeService.createExecutionQuery().executionId(context.getExecutionId()).list();
            for (Execution execution : executions) {
                ExecutionEntity executionEntity = (ExecutionEntity) execution;
                updateInstance(runtimeService, instanceService, executionEntity, 2500);
                // make super fail
                ExecutionEntity superExecution = executionEntity.getSuperExecution();
                while (superExecution != null) {
                    updateInstance(runtimeService, instanceService, superExecution, 2500);
                    superExecution = superExecution.getSuperExecution();
                }
            }
            updateInstanceAct(instanceService, context.getExecutionId(), context.getActivityId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return super.handleIncident(context, message);
    }

    private void updateInstance(RuntimeServiceImpl runtimeService, InstanceService instanceService, ExecutionEntity execution, int code) {
        Instance instance = instanceService.getByInstId(execution.getProcessInstanceId());
        Date endTime = new Date();
        long duration = 0;
        if (instance != null) {
            duration = endTime.getTime() - instance.getStartTime().getTime();
            instance
                    .setEndTime(endTime)
                    .setDuration(duration)
                    .setStatus(InstanceStatus.FAILURE.getStatus())
                    .setCode(code);
            String message = execution.getCurrentActivityId() == null ?
                    execution.getCurrentActivityName() : execution.getCurrentActivityId();
            if (instance.getMsg() == null) {
                instance.setMsg(StatusProperty.getValue(code) + ":" + message);
            } else {
                instance.setMsg(instance.getMsg() + "," + message);
            }
            instanceService.updateDone(instance);
        }
        runtimeService.getCommandExecutor().execute(new UpdateHistoricInstanceCmd(
                execution.getProcessInstanceId(),
                endTime,
                duration,
                HistoricProcessInstance.STATE_EXTERNALLY_TERMINATED));
    }

    private void updateInstanceAct(InstanceService instanceService, String procInstId, String actId) {
        InstanceAct instanceAct = new InstanceAct()
                .setProcInstId(procInstId)
                .setActId(actId)
                .setEndTime(new Date())
                .setStatus(InstanceStatus.FAILURE.getStatus());
        instanceService.updateAct(instanceAct);
    }
}
