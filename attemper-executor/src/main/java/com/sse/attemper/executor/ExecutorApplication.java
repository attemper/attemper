package com.sse.attemper.executor;

import com.sse.attemper.common.constant.GlobalConstants;
import com.sse.attemper.config.conf.ConfigConfiguration;
import com.sse.attemper.core.conf.CoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({
        ConfigConfiguration.class,
        //SysConfiguration.class,
        CoreConfiguration.class
})
@EnableTransactionManagement  //开启事务
@EnableJpaRepositories(GlobalConstants.jpaRepositoryLocation)   //jpa dao
@EntityScan(GlobalConstants.jpaEntityLocation)   //jpa entity
@SpringBootApplication
public class ExecutorApplication {

    /**
     * spring boot app start entrance
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }

}