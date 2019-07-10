package com.github.attemper.scheduler;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.invoker.conf.InvokerConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Import({
        InvokerConfiguration.class
})
@EnableAsync
@EnableDiscoveryClient
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)
@SpringBootApplication
public class SchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
