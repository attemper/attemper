package com.github.attemper.executor.task.wxwork;

import com.github.attemper.alarm.AlarmType;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.ext.notice.MessageBean;
import com.github.attemper.core.ext.notice.channel.wxwork.WxWorkSender;
import com.github.attemper.executor.ext.param.WxWorkParam;
import com.github.attemper.executor.task.ParentTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomWxWorkTask extends ParentTask {

    @Autowired
    private WxWorkSender wxWorkSender;

    @Override
    public void executeIntern(DelegateExecution execution) {
        WxWorkParam param = ReflectUtil.reflectObj(WxWorkParam.class, CUSTOM_WX_WORK, execution.getVariables());

        String prefix = CommonConstants.KEY_ALARM_ARG + AlarmType.WX_WORK.getValue() + CommonConstants.UNDERSCORE;
        Map<String, Object> paramMap = BeanUtil.bean2Map(param);
        Map<String, Object> extraMap = new HashMap<>(paramMap.size());
        paramMap.forEach((k, v) -> {
            extraMap.put(prefix + k, v);
        });
        MessageBean messageBean = new MessageBean()
                .setContent(param.getContent())
                .setExtraMap(extraMap);
        appendLogText(execution, 10002, param);
        wxWorkSender.send(messageBean);
    }

    private static final String CUSTOM_WX_WORK = "customWxWork";
}
