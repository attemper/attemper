package com.sse.attemper.core.conf;

import com.sse.attemper.core.controller.arg.ArgController;
import com.sse.attemper.core.controller.job.JobController;
import com.sse.attemper.core.controller.job.TriggerController;
import com.sse.attemper.core.controller.monitor.RealTimeMonitorController;
import com.sse.attemper.core.controller.project.ProjectController;
import com.sse.attemper.core.controller.tool.ToolController;
import com.sse.attemper.core.dao.mapper.arg.ArgMapper;
import com.sse.attemper.core.dao.mapper.job.JobMapper;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.core.dao.mapper.monitor.JobExecutionMapper;
import com.sse.attemper.core.dao.mapper.monitor.JobInstanceMapper;
import com.sse.attemper.core.dao.mapper.project.ProjectMapper;
import com.sse.attemper.core.service.arg.ArgService;
import com.sse.attemper.core.service.job.JobService;
import com.sse.attemper.core.service.job.TriggerService;
import com.sse.attemper.core.service.monitor.RealTimeMonitorService;
import com.sse.attemper.core.service.project.ProjectService;
import com.sse.attemper.core.service.tool.ToolService;
import com.sse.attemper.sys.conf.SysConfiguration;
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
        ProjectMapper.class,
        JobExecutionMapper.class,
        JobInstanceMapper.class,

        //service
        JobService.class,
        ArgService.class,
        TriggerService.class,
        ProjectService.class,
        ToolService.class,
        RealTimeMonitorService.class,

        //controller
        JobController.class,
        ArgController.class,
        TriggerController.class,
        ProjectController.class,
        ToolController.class,
        RealTimeMonitorController.class
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
