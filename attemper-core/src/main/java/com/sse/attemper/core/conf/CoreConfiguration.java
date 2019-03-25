package com.sse.attemper.core.conf;

import com.sse.attemper.core.controller.arg.ArgController;
import com.sse.attemper.core.controller.job.BaseJobController;
import com.sse.attemper.core.controller.job.TriggerController;
import com.sse.attemper.core.controller.tool.ToolController;
import com.sse.attemper.core.service.arg.ArgService;
import com.sse.attemper.core.service.job.BaseJobService;
import com.sse.attemper.core.service.job.TriggerService;
import com.sse.attemper.core.service.tool.ToolService;
import org.hibernate.validator.HibernateValidator;
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
        ToolController.class
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
