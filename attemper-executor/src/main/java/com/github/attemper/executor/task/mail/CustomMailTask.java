package com.github.attemper.executor.task.mail;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.mail.EmailSender;
import com.github.attemper.executor.constant.PropertyConstants;
import com.github.attemper.executor.task.ParentTask;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomMailTask extends ParentTask implements JavaDelegate {

    @Autowired
    private EmailSender emailSender;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            MessageBean messageBean = buildMessageBean(execution);
            emailSender.send(messageBean);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            super.saveInstanceAct(execution, null, Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), JobInstanceStatus.FAILURE);
        }
    }

    private MessageBean buildMessageBean(DelegateExecution execution) {
        MessageBean messageBean = new MessageBean();
        Collection<ModelElementInstance> elements = execution.getBpmnModelElementInstance().getExtensionElements().getElements();
        elements.forEach(item -> {
            if (item instanceof CamundaPropertiesImpl) {
                CamundaPropertiesImpl cpi = (CamundaPropertiesImpl) item;
                cpi.getCamundaProperties().forEach(cell -> {
                    String camundaValue = cell.getCamundaValue();
                    if (camundaValue != null) {
                        if (PropertyConstants.from.equals(cell.getCamundaName())) {
                            messageBean.setFrom(camundaValue);
                        } else if (PropertyConstants.to.equals(cell.getCamundaName())) {
                            messageBean.setTo(camundaValue);
                        } else if (PropertyConstants.subject.equals(cell.getCamundaName())) {
                            messageBean.setSubject(camundaValue);
                        } else if (PropertyConstants.content.equals(cell.getCamundaName())) {
                            messageBean.setContent(camundaValue);
                        }
                    }
                });
            }
        });
        Object from = execution.getVariable(PropertyConstants.from);
        if (from != null && StringUtils.isNotBlank(from.toString())) {
            messageBean.setFrom(from.toString());
        }
        Object to = execution.getVariable(PropertyConstants.to);
        if (to != null && StringUtils.isNotBlank(to.toString())) {
            messageBean.setTo(to.toString());
        }
        Object subject = execution.getVariable(PropertyConstants.subject);
        if (subject != null && StringUtils.isNotBlank(subject.toString())) {
            messageBean.setSubject(subject.toString());
        }
        Object content = execution.getVariable(PropertyConstants.content);
        if (content != null && StringUtils.isNotBlank(content.toString())) {
            messageBean.setContent(content.toString());
        }
        return messageBean;
    }
}
