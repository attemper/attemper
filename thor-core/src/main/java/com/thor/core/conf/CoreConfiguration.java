package com.thor.core.conf;

import com.thor.core.controller.TenantController;
import com.thor.core.controller.UserController;
import com.thor.core.init.CustomPostConstruct;
import com.thor.core.service.TenantService;
import com.thor.core.service.UserService;
import com.thor.core.controller.TenantController;
import com.thor.core.controller.UserController;
import com.thor.core.service.TenantService;
import com.thor.core.service.UserService;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@ComponentScan(basePackageClasses = {
        //controller
        TenantController.class,
        UserController.class,

        //service
        TenantService.class,
        UserService.class,

        //init
        CustomPostConstruct.class
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
