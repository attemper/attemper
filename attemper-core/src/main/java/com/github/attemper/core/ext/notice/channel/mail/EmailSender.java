package com.github.attemper.core.ext.notice.channel.mail;

import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailSender implements Sender {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Override
    public void send(MessageBean messageBean) {
        if (StringUtils.isBlank(messageBean.getTo().getEmail())) {
            log.error("email is blank:{}", messageBean);
            return;
        }
        MimeMessage email = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setFrom(StringUtils.isNotBlank(messageBean.getFrom())
                    ? messageBean.getFrom() : mailProperties.getUsername());
            helper.setTo(messageBean.getTo().getEmail().split(","));
            helper.setSubject(messageBean.getSubject());
            helper.setText(messageBean.getContent(), true);
            mailSender.send(email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
