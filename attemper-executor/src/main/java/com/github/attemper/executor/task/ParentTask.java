package com.github.attemper.executor.task;

import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.util.CamundaUtil;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ParentTask {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected JobInstanceService jobInstanceService;

    protected void saveLogKey(DelegateExecution execution, String logKey) {
        JobInstanceAct jobInstanceAct = new JobInstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setLogKey(logKey);
        jobInstanceService.updateAct(jobInstanceAct);
    }

    protected void saveLogKey(DelegateExecution execution, int code) {
        this.saveLogKey(execution, String.valueOf(code));
    }

    protected void appendLogText(DelegateExecution execution, String logText) {
        JobInstanceAct jobInstanceAct = new JobInstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setLogText(logText);
        jobInstanceService.updateAct(jobInstanceAct);
    }

    /**
     * extract camunda property to map
     *
     * @param execution
     * @return
     */
    protected Map<String, String> resolveExtensionElement(DelegateExecution execution) {
        Map<String, String> map = new HashMap<>();
        Collection<ModelElementInstance> elements = execution.getBpmnModelElementInstance().getExtensionElements().getElements();
        elements.forEach(item -> {
            if (item instanceof CamundaPropertiesImpl) {
                CamundaPropertiesImpl cpi = (CamundaPropertiesImpl) item;
                cpi.getCamundaProperties().forEach(cell -> {
                    map.put(cell.getCamundaName(), cell.getCamundaValue());
                });
            }
        });
        return map;
    }
}
