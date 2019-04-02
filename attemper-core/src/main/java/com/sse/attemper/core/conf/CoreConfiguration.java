package com.sse.attemper.core.conf;

import com.sse.attemper.core.controller.arg.ArgController;
import com.sse.attemper.core.controller.job.BaseJobController;
import com.sse.attemper.core.controller.job.TriggerController;
import com.sse.attemper.core.controller.tool.ToolController;
import com.sse.attemper.core.dao.mapper.job.BaseJobMapper;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.core.service.arg.ArgService;
import com.sse.attemper.core.service.job.BaseJobService;
import com.sse.attemper.core.service.job.TriggerService;
import com.sse.attemper.core.service.tool.ToolService;
import org.hibernate.validator.HibernateValidator;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
//@EnableConfigurationProperties(SysProperties.class)
@ComponentScan(basePackageClasses = {
        //service
        BaseJobService.class,
        ArgService.class,
        TriggerService.class,
        ToolService.class,

        //controller
        BaseJobController.class,
        ArgController.class,
        TriggerController.class,
        ToolController.class,

        BaseJobMapper.class,
        TriggerMapper.class
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

    /**
     * use it to operate job&trigger with database, you can not start it
     * @param schedulerFactory
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        return schedulerFactory.getScheduler();
    }
}
