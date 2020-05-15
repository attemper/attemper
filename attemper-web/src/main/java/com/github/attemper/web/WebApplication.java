
package com.github.attemper.web;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.invoker.conf.InvokerConfiguration;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
@OpenAPIDefinition(
		info = @Info(
				title = "Attemper api doc",
				version = "1.0",
				description = "Distributed Scheduling application"
		),
		externalDocs = @ExternalDocumentation(description = "User document",
				url = "https://attemper.github.io/attemper-document/"
		)
)
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}



