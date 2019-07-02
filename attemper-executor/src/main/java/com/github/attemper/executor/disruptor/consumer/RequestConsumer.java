package com.github.attemper.executor.disruptor.consumer;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.disruptor.event.JobEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.Date;
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
                runtimeService.startProcessInstanceById(processDefinition.getId(), param.getId());
            } else if (processDefinitions.isEmpty()) {
                log.error("process not exists:{}|{}|{}", param.getId(), param.getJobName(), param.getTenantId());
                throw new RTException(2002);
            } else {
                log.error("process more than 1:{}|{}|{}", param.getId(), param.getJobName(), param.getTenantId());
                throw new RTException(2003);
            }
        } catch (Exception e) {
            if (e instanceof RTException) {
                RTException rte = (RTException) e;
                updateInstance(param, rte.getCode(), rte.getMsg());
                throw rte;
            } else {
                int code;
                if (StringUtils.isNotBlank(param.getTriggerName())) {
                    code = 2000;
                } else {
                    code = 2001;
                }
                updateInstance(param, code, StatusProperty.getValue(code) + ":" + e.getMessage());
                throw new RTException(code, e);
            }
        }
    }

    private void updateInstance(JobInvokingParam param, int code, String msg) {
        JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
        JobInstance jobInstance = jobInstanceService.get(param.getId());
        Date now = new Date();
        jobInstance.setEndTime(now);
        jobInstance.setDuration(now.getTime() - jobInstance.getStartTime().getTime());
        jobInstance.setStatus(JobInstanceStatus.FAILURE.getStatus());
        jobInstance.setCode(code);
        jobInstance.setMsg(msg);
        jobInstanceService.update(jobInstance);
    }
}
