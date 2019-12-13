package com.github.attemper.executor.task.mail;

import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.mail.EmailSender;
import com.github.attemper.executor.task.ParentTask;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomMailTask extends ParentTask {

    @Autowired
    private EmailSender emailSender;

    @Override
    public void executeIntern(DelegateExecution execution) {
        MessageBean messageBean = buildMessageBean(execution);
        emailSender.send(messageBean);
    }

    private MessageBean buildMessageBean(DelegateExecution execution) {
        MessageBean messageBean = new MessageBean();
        Map<String, String> propertyMap = resolveExtensionElement(execution);
        Object from = execution.getVariable(FROM);
        if (from != null && StringUtils.isNotBlank(from.toString())) {
            messageBean.setFrom(from.toString());
        } else {
            messageBean.setFrom(propertyMap.get(FROM));
        }
        Object to = execution.getVariable(TO);
        if (to != null && StringUtils.isNotBlank(to.toString())) {
            messageBean.setTo(new Tenant().setEmail(to.toString()));
        } else {
            messageBean.setTo(new Tenant().setEmail(propertyMap.get(TO)));
        }
        Object subject = execution.getVariable(SUBJECT);
        if (subject != null && StringUtils.isNotBlank(subject.toString())) {
            messageBean.setSubject(subject.toString());
        } else {
            messageBean.setSubject(propertyMap.get(SUBJECT));
        }
        Object content = execution.getVariable(CONTENT);
        if (content != null && StringUtils.isNotBlank(content.toString())) {
            messageBean.setContent(content.toString());
        } else {
            messageBean.setContent(propertyMap.get(CONTENT));
        }
        return messageBean;
    }

    private static final String CUSTOM_MAIL = "custom_mail_";

    private static final String FROM = CUSTOM_MAIL + "from";

    private static final String TO = CUSTOM_MAIL + "to";

    private static final String SUBJECT = CUSTOM_MAIL + "subject";

    private static final String CONTENT = CUSTOM_MAIL + "content";
}
