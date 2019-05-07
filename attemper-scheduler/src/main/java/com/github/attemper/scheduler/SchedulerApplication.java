package com.github.attemper.scheduler;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.config.scheduler.conf.ConfigSchedulerConfiguration;
import com.github.attemper.core.conf.CoreConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
        ConfigSchedulerConfiguration.class,
        CoreConfiguration.class
})
@EnableAsync
@EnableDiscoveryClient
@EnableTransactionManagement
//@EnableJpaRepositories(GlobalConstants.jpaRepositoryLocation)   //jpa dao
//@EntityScan(GlobalConstants.jpaEntityLocation)   //jpa entity
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)   //mybatis dao
@SpringBootApplication
public class SchedulerApplication {

    /**
     * spring boot app start entrance
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
