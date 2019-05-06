package com.github.attemper.config.base.conf;

import com.github.attemper.config.base.aspect.MultiDataSourceAspect;
import com.github.attemper.config.base.bean.ContextBeanAware;
import com.github.attemper.config.base.dao.repo.ApiLogRepository;
import com.github.attemper.config.base.datasource.DataSourceConfig;
import com.github.attemper.config.base.datasource.DataSourceProperties;
import com.github.attemper.config.base.exception.GlobalExceptionAdvicer;
import com.github.attemper.config.base.id.SnowFlakeIdGenerator;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.service.ApiLogService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @auth ldang
 */
@EnableConfigurationProperties(
        AppProperties.class
)
@Configuration
@ComponentScan(basePackageClasses = {
        //多数据源切面
        MultiDataSourceAspect.class,
        //数据源属性
        DataSourceProperties.class,
        //多数据配置
        DataSourceConfig.class,

        //全局异常处理
        GlobalExceptionAdvicer.class,

        //swagger2配置
        Swagger2Config.class,

        //bean factory bean
        ContextBeanAware.class,

        //log
        ApiLogService.class,
        ApiLogRepository.class
})
public class ConfigConfiguration {

    @Autowired
    private AppProperties appProperties;

    @Bean
    public IdGenerator idGenerator() {
        return new SnowFlakeIdGenerator(
                appProperties.getSnowFlake().getDataCenterId(),
                appProperties.getSnowFlake().getMachineId());
    }

}
