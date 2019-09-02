package com.github.attemper.core.service.dispatch;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.datasource.DataSourceGetParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceListParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.result.dispatch.datasource.ConnectionTestResult;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import com.github.attemper.core.dao.dispatch.DataSourceMapper;
import com.github.attemper.java.sdk.common.util.ExceptionUtil;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.store.SysStore;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.StringUtils;
import org.camunda.commons.utils.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Transactional
@Service
public class DataSourceService extends BaseServiceAdapter {

    @Autowired
    private DataSourceMapper mapper;

    public DataSourceInfo get(DataSourceGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public Map<String, Object> list(DataSourceListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<DataSourceInfo> list = (Page<DataSourceInfo>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public List<ConnectionTestResult> testConnection(DataSourceNamesParam param) {
        List<ConnectionTestResult> resultList = new ArrayList<>();
        for (String dbName : param.getDbNames()) {
            DataSource dataSource = getDataSource(dbName, injectTenantId());
            String errorMsg = testConnection(dataSource);
            resultList.add(new ConnectionTestResult().setDbName(dbName).setErrorMsg(errorMsg));
        }
        return resultList;
    }

    private String testConnection(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            return null;
        } catch (SQLException e) {
            return ExceptionUtil.getStackTrace(e);
        }
    }

    public DataSource getDataSource(String dbName, String tenantId) {
        DataSource dataSource = SysStore.getBizDataSource(dbName, tenantId);
        if (dataSource == null) {
            DataSourceInfo dataSourceInfo = get(new DataSourceGetParam().setDbName(dbName));
            if (dataSourceInfo == null) {
                throw new RTException(7150, dbName);
            }
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(dataSourceInfo.getDriverClassName());
            hikariConfig.setJdbcUrl(dataSourceInfo.getJdbcUrl());
            hikariConfig.setUsername(dataSourceInfo.getUserName());
            hikariConfig.setPassword(dataSourceInfo.getPassword());
            if (StringUtils.isBlank(dataSourceInfo.getAttribute())) {
                hikariConfig.setMinimumIdle(5);
                hikariConfig.setMaximumPoolSize(50);
            } else {
                Properties properties = new Properties();
                InputStream is = IoUtil.stringAsInputStream(dataSourceInfo.getAttribute());
                try {
                    properties.load(is);
                } catch (IOException e) {
                    throw new RTException(7102, e);
                }
                hikariConfig.setDataSourceProperties(properties);
            }
            dataSource = new HikariDataSource(hikariConfig);
            SysStore.putBizDataSource(dbName, tenantId, dataSource);
        }
        return dataSource;
    }

}
