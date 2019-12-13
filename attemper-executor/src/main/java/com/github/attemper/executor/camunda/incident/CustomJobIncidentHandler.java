package com.github.attemper.executor.camunda.incident;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.param.dispatch.instance.InstanceActParam;
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
            if (executions.size() > 0) {
                updateInstanceAct(instanceService, executions.get(0).getProcessInstanceId(), context.getActivityId());
            }
            for (Execution execution : executions) {
                ExecutionEntity executionEntity = (ExecutionEntity) execution;
                updateInstance(runtimeService, instanceService, executionEntity);
                // make super fail
                ExecutionEntity superExecution = executionEntity.getSuperExecution();
                while (superExecution != null) {
                    updateInstance(runtimeService, instanceService, superExecution);
                    superExecution = superExecution.getSuperExecution();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return super.handleIncident(context, message);
    }

    private void updateInstance(RuntimeServiceImpl runtimeService, InstanceService instanceService, ExecutionEntity execution) {
        Instance instance = instanceService.getByInstId(execution.getProcessInstanceId());
        long current = System.currentTimeMillis();
        long duration = 0;
        if (instance != null) {
            duration = current - instance.getStartTime();
            instance
                    .setEndTime(current)
                    .setDuration(duration)
                    .setStatus(InstanceStatus.FAILURE.getStatus());
            instanceService.updateDone(instance);
        }
        runtimeService.getCommandExecutor().execute(new UpdateHistoricInstanceCmd(
                execution.getProcessInstanceId(),
                new Date(current),
                duration,
                HistoricProcessInstance.STATE_EXTERNALLY_TERMINATED));
    }

    private void updateInstanceAct(InstanceService instanceService, String procInstId, String actId) {
        List<InstanceAct> instanceActs = instanceService.listAct(new InstanceActParam().setProcInstId(procInstId).setActId(actId));
        long current = System.currentTimeMillis();
        if (instanceActs.size() > 0) {
            InstanceAct instanceAct = new InstanceAct()
                    .setProcInstId(procInstId)
                    .setActId(actId)
                    .setEndTime(current)
                    .setDuration(current - instanceActs.get(0).getStartTime())
                    .setStatus(InstanceStatus.FAILURE.getStatus());
            instanceService.updateAct(instanceAct);
        }
    }
}
