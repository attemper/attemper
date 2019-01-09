package com.thor.config.conf;

import com.thor.config.aspect.MultiDataSourceAspect;
import com.thor.config.bean.ContextBeanAware;
import com.thor.config.dao.repo.ApiLogRepository;
import com.thor.config.datasource.DataSourceConfig;
import com.thor.config.datasource.DataSourceProperties;
import com.thor.config.exception.GlobalExceptionAdvicer;
import com.thor.config.properties.StarkAppProperties;
import com.thor.config.service.ApiLogService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @auth ldang
 */
@Configuration
@EnableConfigurationProperties({StarkAppProperties.class})
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

}
