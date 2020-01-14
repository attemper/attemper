package com.github.attemper.executor.task.mail;

import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.mail.EmailSender;
import com.github.attemper.executor.ext.param.MailParam;
import com.github.attemper.executor.task.ParentTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomMailTask extends ParentTask {

    @Autowired
    private EmailSender emailSender;

    @Override
    public void executeIntern(DelegateExecution execution) {
        MailParam param = (MailParam) ReflectUtil.reflectObj(MailParam.class, CUSTOM_MAIL, execution.getVariables());
        MessageBean messageBean = new MessageBean()
                .setFrom(param.getFrom())
                .setTo(new Tenant().setEmail(param.getTo()))
                .setSubject(param.getSubject())
                .setContent(param.getContent());
        appendLogText(execution, 10002, param);
        emailSender.send(messageBean);
    }

    private static final String CUSTOM_MAIL = "customMail";
}
