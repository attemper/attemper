package com.github.attemper.config.base.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置数据源
 * @auth ldang
 */
@Configuration
public class DataSourceConfig {
	
	@Autowired
	private DataSourceProperties properties;
    
    @Bean
    @Primary
    public DynamicDataSource dataSource() {
        Map<String, DataSource> targetDataSources = new HashMap<String, DataSource>();
        targetDataSources.put(DataSourceName.FIRST, properties.getFirst());
        targetDataSources.put(DataSourceName.SECOND, properties.getSecond());
        return new DynamicDataSource(targetDataSources.get(DataSourceName.FIRST), targetDataSources);
    }
}
