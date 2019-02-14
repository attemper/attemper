package com.thor.core.conf;

import com.thor.core.controller.arg.ArgController;
import com.thor.core.controller.job.BaseJobController;
import com.thor.core.service.arg.ArgService;
import com.thor.core.service.job.BaseJobService;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
//@EnableConfigurationProperties(CoreProperties.class)
@ComponentScan(basePackageClasses = {
        //service
        BaseJobService.class,
        ArgService.class,

        //controller
        BaseJobController.class,
        ArgController.class
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
