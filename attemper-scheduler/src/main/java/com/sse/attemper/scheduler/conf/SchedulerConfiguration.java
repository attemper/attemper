package com.sse.attemper.scheduler.conf;

import com.sse.attemper.autoconfigure.GrpcClientConfiguration;
import com.sse.attemper.factory.DiscoveryClientChannelFactory;
import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(
        GrpcClientConfiguration.class
)
@Configuration
public class SchedulerConfiguration {

    @Bean
    public Channel channel(DiscoveryClientChannelFactory channelFactory) {
        return channelFactory.create();
    }

}
