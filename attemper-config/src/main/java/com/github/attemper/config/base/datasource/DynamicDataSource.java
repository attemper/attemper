package com.github.attemper.config.base.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 * @auth ldang
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> dynamicDataSourceHolder = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, DataSource> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        dynamicDataSourceHolder.set(dataSource);
    }

    public static String getDataSource() {
        return dynamicDataSourceHolder.get();
    }

    public static void clearDataSource() {
        dynamicDataSourceHolder.remove();
    }

}
