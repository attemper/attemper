
package com.github.attemper.web;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.invoker.conf.InvokerConfiguration;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
		InvokerConfiguration.class
})
@EnableAsync
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)
@SpringBootApplication
@EnableProcessApplication(GlobalConstants.defaultContextPath)
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}



