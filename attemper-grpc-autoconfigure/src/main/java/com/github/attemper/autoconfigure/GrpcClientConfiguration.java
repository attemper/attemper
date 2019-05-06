package com.github.attemper.autoconfigure;

import com.github.attemper.factory.DiscoveryClientChannelFactory;
import com.github.attemper.properties.GrpcClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({GrpcClientProperties.class})
@Configuration
public class GrpcClientConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public DiscoveryClientChannelFactory channelFactory(DiscoveryClient client, GrpcClientProperties properties) {
        return new DiscoveryClientChannelFactory(client, properties);
    }

}
