package com.github.attemper.executor;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.security.conf.SecurityConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
        SecurityConfiguration.class
})
@EnableAsync
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)
@SpringBootApplication
public class ExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }

}