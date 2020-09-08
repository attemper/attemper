package com.github.attemper.executor.disruptor.consumer;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.InstanceActParam;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.executor.disruptor.event.JobEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;

import java.util.List;

/**
 * consumer of request
 */
@Slf4j
public class RequestConsumer implements WorkHandler<JobEvent> {

    @Override
    public void onEvent(JobEvent event) throws Exception {
        RepositoryService repositoryService = SpringContextAware.getBean(RepositoryService.class);
        RuntimeService runtimeService = SpringContextAware.getBean(RuntimeService.class);
        JobInvokingParam param = event.getParam();
        try {
            List<ProcessDefinition> processDefinitions = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(param.getJobName())
                    .tenantIdIn(param.getTenantId())
                    .latestVersion()
                    .list();
            if (processDefinitions.size() == 1) {
                ProcessDefinition processDefinition = processDefinitions.get(0);
                ProcessInstantiationBuilder builder = runtimeService.createProcessInstanceById(processDefinition.getId())
                        .businessKey(param.getId())
                        .setVariables(param.getVariableMap());
                if (param.getBeforeActIds() != null) {
                    for (String beforeActId : param.getBeforeActIds()) {
                        builder.startBeforeActivity(beforeActId);
                    }
                }
                if (param.getAfterActIds() != null) {
                    for (String afterActId : param.getAfterActIds()) {
                        builder.startAfterActivity(afterActId);
                    }
                }
                builder.execute();
            } else if (processDefinitions.isEmpty()) {
                log.error("process not exists:{}|{}|{}", param.getId(), param.getJobName(), param.getTenantId());
                throw new RTException(2002);
            } else {
                log.error("process more than 1:{}|{}|{}", param.getId(), param.getJobName(), param.getTenantId());
                throw new RTException(2003);
            }
        } catch (Exception e) {
            RTException rte;
            if (e instanceof RTException) {
                rte = (RTException) e;
            } else {
                int code;
                if (StringUtils.isNotBlank(param.getTriggerName())) {
                    code = 2000;
                } else {
                    code = 2001;
                }
                rte = new RTException(code, e);
            }

            InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
            Instance instance = instanceService.get(param.getId());
            long current = System.currentTimeMillis();
            instance.setEndTime(current).setDuration(current - instance.getStartTime())
                    .setStatus(InstanceStatus.FAILURE.getStatus());
            boolean hasRunning = false;
            if (instance.getProcInstId() != null) {
                List<InstanceAct> instanceActs = instanceService.listAct(new InstanceActParam().setProcInstId(instance.getProcInstId()));
                for (InstanceAct instanceAct : instanceActs) {
                    if (InstanceStatus.RUNNING.getStatus() == instanceAct.getStatus()) {
                        // handle running act
                        hasRunning = true;
                        instanceAct
                                .setEndTime(current)
                                .setDuration(current - instanceAct.getStartTime())
                                .setStatus(InstanceStatus.FAILURE.getStatus())
                                .setLogText(e.getMessage()); // will append
                        instanceService.updateAct(instanceAct);
                    }
                }
            }
            if (!hasRunning) {
                instance.setCode(rte.getCode()).setMsg(rte.getMsg());
            }
            instanceService.updateDone(instance);
            throw rte;
        }
    }
}
