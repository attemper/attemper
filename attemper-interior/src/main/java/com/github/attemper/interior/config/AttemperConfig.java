package com.github.attemper.interior.config;

import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.java.sdk.micro.biz2executor.client.Biz2ExecutorMicroClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttemperConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public Biz2ExecutorMicroClient biz2ExecutorMicroClient() {
        Biz2ExecutorMicroClient client = new Biz2ExecutorMicroClient();
        client.loginParam(new LoginParam().setUserName("interior").setPassword("1"));
        client.serviceName("executor");
        client.discoveryClient(discoveryClient);
        client.initialize();
        return client;
    }

}
