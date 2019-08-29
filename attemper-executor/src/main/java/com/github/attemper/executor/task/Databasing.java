package com.github.attemper.executor.task;

import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.datasource.DynamicDataSource;
import com.github.attemper.core.service.dispatch.DataSourceService;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public interface Databasing {

    String SELECT = "select";

    String UPDATE = "update";

    String INSERT = "insert";

    String DELETE = "delete";

    String DB_NAME = "db_name";

    String SQL = "sql";

    default JdbcTemplate injectJdbcTemplate(DelegateExecution execution) {
        String dbName = (String) execution.getVariables().get(DB_NAME);
        DataSource dataSource;
        if (StringUtils.isBlank(dbName)) {
            dataSource = SpringContextAware.getBean(DynamicDataSource.class);  //use attemper datasource
        } else {
            dataSource = SpringContextAware.getBean(DataSourceService.class).getDataSource(dbName, execution.getTenantId());
        }
        return new JdbcTemplate(dataSource);
    }

    default String injectSql(DelegateExecution execution) {
        return (String) execution.getVariables().get(SQL);
    }

    default int[] insert(DelegateExecution execution, List<Object[]> data) {
        return injectJdbcTemplate(execution).batchUpdate(injectSql(execution), data);
    }
}
