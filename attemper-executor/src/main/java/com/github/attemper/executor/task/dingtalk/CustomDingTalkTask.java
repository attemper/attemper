package com.github.attemper.executor.task.dingtalk;

import com.github.attemper.alarm.AlarmType;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.dingtalk.DingTalkSender;
import com.github.attemper.executor.ext.param.DingTalkParam;
import com.github.attemper.executor.task.ParentTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomDingTalkTask extends ParentTask {

    @Autowired
    private DingTalkSender dingTalkSender;

    @Override
    public void executeIntern(DelegateExecution execution) {
        DingTalkParam param = ReflectUtil.reflectObj(DingTalkParam.class, CUSTOM_DING_TALK, execution.getVariables());

        String prefix = CommonConstants.KEY_ALARM_ARG + AlarmType.DING_TALK.getValue() + CommonConstants.UNDERSCORE;
        Map<String, Object> paramMap = BeanUtil.bean2Map(param);
        Map<String, Object> extraMap = new HashMap<>(paramMap.size());
        paramMap.forEach((k, v) -> {
            extraMap.put(prefix + k, v);
        });
        MessageBean messageBean = new MessageBean()
                .setSubject(param.getSubject())
                .setContent(param.getContent())
                .setExtraMap(extraMap);
        appendLogText(execution, 10002, param);
        try {
            dingTalkSender.send(messageBean);
        } catch (Exception e) {
            throw new RTException(1901, e);
        }
    }

    private static final String CUSTOM_DING_TALK = "customDingTalk";
}
