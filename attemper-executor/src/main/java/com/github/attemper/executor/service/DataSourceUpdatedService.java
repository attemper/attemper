package com.github.attemper.executor.service;

import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.store.SysStore;
import org.springframework.stereotype.Service;

@Service
public class DataSourceUpdatedService extends BaseServiceAdapter {

    public Void remove(DataSourceNamesParam param) {
        for (String dbName : param.getDbNames()) {
            SysStore.removeBizDataSource(dbName, injectTenantId());
        }
        return null;
    }
}
