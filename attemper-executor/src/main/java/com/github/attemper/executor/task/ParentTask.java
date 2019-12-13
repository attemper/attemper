package com.github.attemper.executor.task;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.property.TipProperty;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.executor.util.CamundaUtil;
import com.github.attemper.java.sdk.common.util.DateUtil;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ParentTask implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        String jobName = CamundaUtil.extractKeyFromProcessDefinitionId(execution.getProcessDefinitionId());
        appendLogText(execution, 10000, jobName, execution.getCurrentActivityId());
        try {
            executeIntern(execution);
            appendLogText(execution,10001, jobName, execution.getCurrentActivityId());
        } catch (Exception e) {
            saveLogKeyThenThrowException(execution, e);
        }
    }

    protected abstract void executeIntern(DelegateExecution execution);

    @Autowired
    protected InstanceService instanceService;

    protected void saveLogKeyThenThrowException(DelegateExecution execution, Exception e) {
        int code;
        if (e instanceof RTException) {
            code = ((RTException) e).getCode();
        } else {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        InstanceAct instanceAct = new InstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setLogKey(String.valueOf(code));
        instanceService.updateAct(instanceAct);
        throw (e instanceof RTException ? ((RTException) e) : new RTException(code, e));
    }

    protected void appendLogText(DelegateExecution execution, int key, Object... params) {
        String tip = TipProperty.getValue(key);
        StringBuilder sb = new StringBuilder(DateUtil.format(new Date(), DateUtil.DATE_FORMAT_YYYYMMDDHHMMSSSSS))
                .append(CommonConstants.COLON)
                .append(params == null ? tip : String.format(tip, params))
                .append(CommonConstants.BR);
        InstanceAct instanceAct = new InstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(execution.getActivityInstanceId()))
                .setLogText(sb.toString());
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
