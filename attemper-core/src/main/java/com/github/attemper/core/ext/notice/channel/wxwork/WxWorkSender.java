package com.github.attemper.core.ext.notice.channel.wxwork;

import com.github.attemper.alarm.AlarmType;
import com.github.attemper.alarm.ContentEntity;
import com.github.attemper.alarm.Information;
import com.github.attemper.alarm.wxwork.WxWorkAlarm;
import com.github.attemper.alarm.wxwork.WxWorkConfig;
import com.github.attemper.alarm.wxwork.param.markdown.MarkdownMsg;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.Sender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WxWorkSender implements Sender {

    @Override
    public void send(MessageBean messageBean) throws Exception {
        WxWorkAlarm alarm = new WxWorkAlarm();
        WxWorkConfig config = ReflectUtil.reflectObj(WxWorkConfig.class,
                CommonConstants.KEY_ALARM_ARG + getIndex(),
                messageBean.getExtraMap());
        Information info = new MarkdownMsg().setMarkdown(new ContentEntity().setContent(messageBean.getContent().replace("<br>", "\n")));
        alarm.send(config, info);
    }

    @Override
    public int getIndex() {
        return AlarmType.WX_WORK.getValue();
    }
}
