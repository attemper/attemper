package com.github.attemper.executor.task;

import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.core.service.instance.InstanceService;
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
    protected InstanceService instanceService;

    protected void saveLogKey(DelegateExecution execution, String logKey) {
        InstanceAct instanceAct = new InstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setLogKey(logKey);
        instanceService.updateAct(instanceAct);
    }

    protected void saveLogKey(DelegateExecution execution, int code) {
        this.saveLogKey(execution, String.valueOf(code));
    }

    protected void appendLogText(DelegateExecution execution, String logText) {
        InstanceAct instanceAct = new InstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setLogText(logText);
        instanceService.updateAct(instanceAct);
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
