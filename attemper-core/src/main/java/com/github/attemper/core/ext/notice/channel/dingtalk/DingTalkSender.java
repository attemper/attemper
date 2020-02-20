package com.github.attemper.core.ext.notice.channel.dingtalk;

import com.github.attemper.alarm.AlarmType;
import com.github.attemper.alarm.Information;
import com.github.attemper.alarm.dingtalk.DingTalkAlarm;
import com.github.attemper.alarm.dingtalk.DingTalkConfig;
import com.github.attemper.alarm.dingtalk.param.markdown.MarkdownBody;
import com.github.attemper.alarm.dingtalk.param.markdown.MarkdownMsg;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.Sender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DingTalkSender implements Sender {

    @Override
    public void send(MessageBean messageBean) {
        DingTalkAlarm alarm = new DingTalkAlarm();
        DingTalkConfig config = ReflectUtil.reflectObj(DingTalkConfig.class,
                CommonConstants.KEY_ALARM_ARG + getIndex(),
                messageBean.getExtraMap());
        Information info = new MarkdownMsg().setMarkdown(
                new MarkdownBody()
                        .setTitle(messageBean.getSubject())
                        .setText(messageBean.getContent().replace(CommonConstants.BR, "\n\n")));
        try {
            alarm.send(config, info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public int getIndex() {
        return AlarmType.DING_TALK.getValue();
    }
}
