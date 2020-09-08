package com.github.attemper.executor.task;

import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.datasource.DynamicDataSource;
import com.github.attemper.core.service.dispatch.DataSourceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public interface Databasing {

    String SELECT = "select";

    String UPDATE = "update";

    String INSERT = "insert";

    String DELETE = "delete";

    default JdbcTemplate injectJdbcTemplate(String dbName, String tenantId) {
        DataSource dataSource;
        if (StringUtils.isBlank(dbName)) {
            dataSource = SpringContextAware.getBean(DynamicDataSource.class);  //use attemper datasource
        } else {
            dataSource = SpringContextAware.getBean(DataSourceService.class).getDataSource(dbName, tenantId);
        }
        return new JdbcTemplate(dataSource);
    }

}
