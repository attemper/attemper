package com.sse.attemper.scheduler;

import com.sse.attemper.autoconfigure.GrpcClientConfiguration;
import com.sse.attemper.common.constant.GlobalConstants;
import com.sse.attemper.config.conf.ConfigConfiguration;
import com.sse.attemper.sys.conf.SysConfiguration;
import com.sse.quartz.spring.boot.autoconfigure.SseQuartzAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
        ConfigConfiguration.class,
        SysConfiguration.class,
        SseQuartzAutoConfiguration.class,
        GrpcClientConfiguration.class,
})
@EnableAsync
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableJpaRepositories(GlobalConstants.jpaRepositoryLocation)   //jpa dao
@EntityScan(GlobalConstants.jpaEntityLocation)   //jpa entity
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)   //mybatis dao
@SpringBootApplication(exclude = {QuartzAutoConfiguration.class})
public class SchedulerApplication {

    /**
     * spring boot app start entrance
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
