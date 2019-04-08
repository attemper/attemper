package com.sse.attemper.scheduler.conf;

import com.sse.attemper.factory.DiscoveryClientChannelFactory;
import io.grpc.Channel;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

    @Bean
    public Channel channel(DiscoveryClientChannelFactory channelFactory) {
        return channelFactory.create();
    }

    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        return scheduler;
    }

}
