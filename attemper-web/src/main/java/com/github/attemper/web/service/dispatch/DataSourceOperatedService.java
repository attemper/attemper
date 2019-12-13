package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.param.dispatch.datasource.DataSourceNameParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceSaveParam;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import com.github.attemper.core.dao.dispatch.DataSourceMapper;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.core.service.dispatch.DataSourceService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.store.SysStore;
import com.github.attemper.web.ext.app.ExecutorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class DataSourceOperatedService extends BaseServiceAdapter {

    @Autowired
    private DataSourceService service;

    @Autowired
    private DataSourceMapper mapper;

    @Autowired
    private ExecutorHandler executorHandler;

    @Autowired
    private ProjectService projectService;

    public DataSourceInfo add(DataSourceSaveParam param) {
        DataSourceInfo arg = service.get(new DataSourceNameParam().setDbName(param.getDbName()));
        if (arg != null) {
            throw new DuplicateKeyException(param.getDbName());
        }
        arg = toDataSource(param);
        mapper.add(arg);
        return arg;
    }

    public DataSourceInfo update(DataSourceSaveParam param) {
        DataSourceInfo oldDataSourceInfo = service.get(new DataSourceNameParam().setDbName(param.getDbName()));
        if (oldDataSourceInfo == null) {
            return add(param);
        }
        DataSourceInfo updatedDataSourceInfo = toDataSource(param);
        mapper.update(updatedDataSourceInfo);
        // remove data source from store
        SysStore.removeBizDataSource(param.getDbName(), injectTenantId());
        List<String> dbNames = new ArrayList<>();
        dbNames.add(param.getDbName());
        DataSourceNamesParam dataSourceNamesParam = new DataSourceNamesParam().setDbNames(dbNames);
        for (String url : projectService.toExecutorUrls()) {
            executorHandler.removeDataSource(url, dataSourceNamesParam);
        }
        return updatedDataSourceInfo;
    }

    public Void remove(DataSourceNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.delete(paramMap);
        // remove data source from store
        for (String dbName : param.getDbNames()) {
            SysStore.removeBizDataSource(dbName, injectTenantId());
        }
        for (String url : projectService.toExecutorUrls()) {
            executorHandler.removeDataSource(url, param);
        }
        return null;
    }

    private DataSourceInfo toDataSource(DataSourceSaveParam param) {
        return new DataSourceInfo()
                .setDbName(param.getDbName())
                .setDriverClassName(param.getDriverClassName())
                .setJdbcUrl(param.getJdbcUrl())
                .setUserName(param.getUserName())
                .setPassword(param.getPassword())
                .setAttribute(param.getAttribute())
                .setTenantId(injectTenantId());
    }

}
