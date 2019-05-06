
package com.github.attemper.web;

import com.github.attemper.security.conf.SecurityConfiguration;
import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.config.scheduler.conf.ConfigSchedulerConfiguration;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ldang
 */
@Import({
		ConfigSchedulerConfiguration.class,
		SecurityConfiguration.class
})
@EnableAsync
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableJpaRepositories(GlobalConstants.jpaRepositoryLocation)   //jpa dao
@EntityScan(GlobalConstants.jpaEntityLocation)   //jpa entity
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)   //mybatis dao
@SpringBootApplication
@EnableProcessApplication(GlobalConstants.defaultContextPath)
public class WebApplication {

	/**
	 * spring boot app start entrance
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}



