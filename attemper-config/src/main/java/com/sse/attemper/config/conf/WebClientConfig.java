package com.sse.attemper.config.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancerExchangeFilterFunction lbFunction;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(lbFunction)
                .build();
    }

}
