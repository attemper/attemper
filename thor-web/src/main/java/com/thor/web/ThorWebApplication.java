
package com.thor.web;

import com.thor.common.constant.GlobalConstants;
import com.thor.config.conf.ConfigConfiguration;
import com.thor.core.conf.CoreConfiguration;
import com.thor.security.conf.SecurityConfiguration;
import com.thor.sys.conf.SysConfiguration;
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
 * 平台服务启动类
 * @auth ldang
 */
@Import({
		ConfigConfiguration.class,
		SysConfiguration.class,
		CoreConfiguration.class,
		SecurityConfiguration.class
})
@EnableDiscoveryClient  //开启服务发现
@EnableTransactionManagement  //开启事务
@EnableJpaRepositories(GlobalConstants.jpaRepositoryLocation)   //jpa dao
@EntityScan(GlobalConstants.jpaEntityLocation)   //jpa entity
@MapperScan(GlobalConstants.mybatisPlusMapperLocation)   //mybatis dao
@EnableAsync  //开启异步执行
@SpringBootApplication
public class ThorWebApplication {

	/**
	 * spring boot app start entrance
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ThorWebApplication.class, args);
	}
}



