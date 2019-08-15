package com.github.attemper.config.base.conf;

import com.github.attemper.config.base.aspect.MultiDataSourceAspect;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.dao.ApiLogMapper;
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

@EnableConfigurationProperties(
        AppProperties.class
)
@Configuration
@ComponentScan(basePackageClasses = {
        MultiDataSourceAspect.class,
        DataSourceProperties.class,
        DataSourceConfig.class,

        GlobalExceptionAdvisor.class,

        Swagger2Config.class,
        LocalServerConfig.class,

        SpringContextAware.class,

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
