package com.github.attemper.executor.task.mail;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.mail.MailSender;
import com.github.attemper.executor.ext.param.MailParam;
import com.github.attemper.executor.task.ParentTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomMailTask extends ParentTask {

    @Autowired
    private MailSender mailSender;

    @Override
    public void executeIntern(DelegateExecution execution) {
        MailParam param = ReflectUtil.reflectObj(MailParam.class, CUSTOM_MAIL, execution.getVariables());

        MessageBean messageBean = new MessageBean()
                .setFrom(param.getFrom())
                .setTo(new Tenant().setEmail(param.getTo()))
                .setSubject(param.getSubject())
                .setContent(param.getContent())
                .setExtraMap(execution.getVariables());
        appendLogText(execution, 10002, param);
        try {
            mailSender.send(messageBean);
        } catch (Exception e) {
            throw new RTException(1900, e);
        }
    }

    private static final String CUSTOM_MAIL = "customMail";
}
