package com.github.attemper.core.conf;

import com.github.attemper.core.controller.ServerInfoController;
import com.github.attemper.core.dao.application.GistMapper;
import com.github.attemper.core.dao.application.ProjectMapper;
import com.github.attemper.core.dao.dispatch.*;
import com.github.attemper.core.dao.instance.InstanceMapper;
import com.github.attemper.core.ext.condition.ConditionStrategyService;
import com.github.attemper.core.ext.notice.NoticeService;
import com.github.attemper.core.ext.notice.channel.dingtalk.DingTalkSender;
import com.github.attemper.core.ext.notice.channel.mail.MailSender;
import com.github.attemper.core.ext.notice.channel.wxwork.WxWorkSender;
import com.github.attemper.core.service.application.GistService;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.core.service.dispatch.ArgService;
import com.github.attemper.core.service.dispatch.CalendarService;
import com.github.attemper.core.service.dispatch.DataSourceService;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.sys.conf.SysConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        SysConfiguration.class
})
@Configuration
@ComponentScan(basePackageClasses = {
        JobMapper.class,
        ArgMapper.class,
        DataSourceMapper.class,
        ProjectMapper.class,
        GistMapper.class,
        InstanceMapper.class,
        CalendarMapper.class,
        DelayJobMapper.class,

        //service
        JobService.class,
        ArgService.class,
        DataSourceService.class,
        ProjectService.class,
        GistService.class,
        ToolService.class,
        InstanceService.class,
        CalendarService.class,
        ConditionStrategyService.class,

        ServerInfoController.class,

        MailSender.class,
        DingTalkSender.class,
        WxWorkSender.class,
        NoticeService.class,
})
public class CoreConfiguration {

}
