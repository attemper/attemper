package com.github.attemper.core.conf;

import com.github.attemper.core.controller.arg.ArgController;
import com.github.attemper.core.controller.datasource.DataSourceController;
import com.github.attemper.core.controller.project.ProjectController;
import com.github.attemper.core.controller.statistics.CountController;
import com.github.attemper.core.controller.tool.ToolController;
import com.github.attemper.core.dao.mapper.arg.ArgMapper;
import com.github.attemper.core.dao.mapper.calendar.CalendarMapper;
import com.github.attemper.core.dao.mapper.datasource.DataSourceMapper;
import com.github.attemper.core.dao.mapper.delay.DelayJobMapper;
import com.github.attemper.core.dao.mapper.instance.JobInstanceMapper;
import com.github.attemper.core.dao.mapper.job.JobMapper;
import com.github.attemper.core.dao.mapper.job.TriggerMapper;
import com.github.attemper.core.dao.mapper.project.ProjectMapper;
import com.github.attemper.core.dao.mapper.statistics.CountMapper;
import com.github.attemper.core.service.arg.ArgService;
import com.github.attemper.core.service.calendar.CalendarService;
import com.github.attemper.core.service.datasource.DataSourceService;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.job.TriggerService;
import com.github.attemper.core.service.project.ProjectService;
import com.github.attemper.core.service.statistics.CountService;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.sys.conf.SysConfiguration;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Import({
        SysConfiguration.class
})
@Configuration
@ComponentScan(basePackageClasses = {
        //dao.mapper
        JobMapper.class,
        TriggerMapper.class,
        ArgMapper.class,
        DataSourceMapper.class,
        ProjectMapper.class,
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
        JobInstanceService.class,
        CalendarService.class,
        CountService.class,

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
