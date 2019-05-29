package com.github.attemper.invoker.conf;

import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.security.conf.SecurityConfiguration;
import com.github.quartz.spring.boot.autoconfigure.SseQuartzAutoConfiguration;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        SecurityConfiguration.class,
        SseQuartzAutoConfiguration.class
})
@EnableAutoConfiguration(exclude = {
        QuartzAutoConfiguration.class
})
@Configuration
@ComponentScan(basePackageClasses = {
        JobCallingService.class
})
public class InvokerConfiguration {

    @Autowired
    private AppProperties appProperties;

    /**
     * @param schedulerFactory
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        if (appProperties.getWeb().isEnableScheduling()) {
            if (appProperties.getScheduler().getDelayedInSecond() > 0) {
                scheduler.startDelayed(appProperties.getScheduler().getDelayedInSecond());
            } else {
                scheduler.start();
            }
        }
        return scheduler;
    }
}
