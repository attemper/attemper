package com.github.attemper.config.base.conf;

import com.github.attemper.config.base.aspect.MultiDataSourceAspect;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.dao.mapper.ApiLogMapper;
import com.github.attemper.config.base.datasource.DataSourceConfig;
import com.github.attemper.config.base.datasource.DataSourceProperties;
import com.github.attemper.config.base.exception.GlobalExceptionAdvisor;
import com.github.attemper.config.base.id.SnowFlakeIdGenerator;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.service.ApiLogService;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author ldang
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
        GlobalExceptionAdvisor.class,

        //swagger2配置
        Swagger2Config.class,
        LocalServerConfig.class,

        //bean factory bean
        SpringContextAware.class,

        //log
        ApiLogService.class,
        ApiLogMapper.class
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

    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("DB2", "db2");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
