package com.github.attemper.interior;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.config.base.conf.ConfigConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
        ConfigConfiguration.class
})
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)   //mybatis dao
@SpringBootApplication
public class InteriorApplication {

    /**
     * spring boot app start entrance
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(InteriorApplication.class, args);
    }

}