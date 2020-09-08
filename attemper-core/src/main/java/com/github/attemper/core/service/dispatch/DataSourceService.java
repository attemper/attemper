package com.github.attemper.core.service.dispatch;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNameParam;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class DataSourceService extends BaseServiceAdapter {

    @Autowired
    private DataSourceMapper mapper;

    public DataSourceInfo get(DataSourceNameParam param) {
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
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return null;
        } catch (SQLException e) {
            return ExceptionUtil.getStackTrace(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public DataSource getDataSource(String dbName, String tenantId) {
        DataSource dataSource = SysStore.getBizDataSource(dbName, tenantId);
        if (dataSource == null) {
            DataSourceInfo dataSourceInfo = get(new DataSourceNameParam().setDbName(dbName));
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
                hikariConfig.setMaximumPoolSize(20);
            } else {
                String[] confArray = dataSourceInfo.getAttribute().split("\n");
                for (String conf : confArray) {
                    String[] keyValue = conf.split("=");
                    if (keyValue.length == 2) {
                        hikariConfig.addDataSourceProperty(keyValue[0].trim(), keyValue[1].trim());
                    } else {
                        log.error(conf);
                    }
                }
            }
            dataSource = new HikariDataSource(hikariConfig);
            SysStore.putBizDataSource(dbName, tenantId, dataSource);
        }
        return dataSource;
    }

}
