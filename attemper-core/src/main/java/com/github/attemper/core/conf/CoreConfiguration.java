package com.github.attemper.core.conf;

import com.github.attemper.core.controller.application.ProjectController;
import com.github.attemper.core.controller.dispatch.ArgController;
import com.github.attemper.core.controller.dispatch.DataSourceController;
import com.github.attemper.core.controller.statistics.CountController;
import com.github.attemper.core.controller.tool.ToolController;
import com.github.attemper.core.dao.application.ProgramMapper;
import com.github.attemper.core.dao.application.ProjectMapper;
import com.github.attemper.core.dao.dispatch.*;
import com.github.attemper.core.dao.instance.JobInstanceMapper;
import com.github.attemper.core.dao.statistics.CountMapper;
import com.github.attemper.core.ext.notice.NoticeService;
import com.github.attemper.core.ext.notice.channel.mail.EmailSender;
import com.github.attemper.core.service.application.ProgramService;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.core.service.dispatch.*;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.core.service.statistics.CountService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.sys.conf.SysConfiguration;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Import({
        MailSenderAutoConfiguration.class,
        SysConfiguration.class
})
@Configuration
@ComponentScan(basePackageClasses = {
        JobMapper.class,
        TriggerMapper.class,
        ArgMapper.class,
        DataSourceMapper.class,
        ProjectMapper.class,
        ProgramMapper.class,
        JobInstanceMapper.class,
        CalendarMapper.class,
        CountMapper.class,
        DelayJobMapper.class,

        //service
        JobService.class,
        ArgService.class,
        DataSourceService.class,
        TriggerService.class,
        ProjectService.class,
        ToolService.class,
        ProgramService.class,
        JobInstanceService.class,
        CalendarService.class,
        CountService.class,

        EmailSender.class,
        NoticeService.class,

        //controller
        ArgController.class,
        DataSourceController.class,
        ProjectController.class,
        ToolController.class,
        CountController.class
})
public class CoreConfiguration {

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }

}
