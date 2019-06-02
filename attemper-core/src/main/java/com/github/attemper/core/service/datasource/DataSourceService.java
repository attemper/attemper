package com.github.attemper.core.service.datasource;

import com.github.attemper.common.param.dispatch.datasource.DataSourceGetParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceListParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceSaveParam;
import com.github.attemper.common.result.dispatch.datasource.ConnectionTestResult;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import com.github.attemper.core.dao.mapper.datasource.DataSourceMapper;
import com.github.attemper.core.service.tool.ToolService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class DataSourceService extends BaseServiceAdapter {

    @Autowired
    private DataSourceMapper mapper;

    @Autowired
    private ToolService toolService;

    public DataSourceInfo get(DataSourceGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public DataSourceInfo add(DataSourceSaveParam param) {
        DataSourceInfo arg = get(DataSourceGetParam.builder().dbName(param.getDbName()).build());
        if (arg != null) {
            throw new DuplicateKeyException(param.getDbName());
        }
        arg = toDataSource(param);
        mapper.add(arg);
        return arg;
    }

    public DataSourceInfo update(DataSourceSaveParam param) {
        DataSourceInfo oldDataSourceInfo = get(DataSourceGetParam.builder().dbName(param.getDbName()).build());
        if (oldDataSourceInfo == null) {
            return add(param);
        }
        DataSourceInfo updatedDataSourceInfo = toDataSource(param);
        mapper.update(updatedDataSourceInfo);
        return updatedDataSourceInfo;
    }

    public Map<String, Object> list(DataSourceListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<DataSourceInfo> list = (Page<DataSourceInfo>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Void remove(DataSourceNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.delete(paramMap);
        return null;
    }

    public List<ConnectionTestResult> testConnection(DataSourceNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        List<DataSourceInfo> dataSources = mapper.getByNames(paramMap);
        List<ConnectionTestResult> resultList = new ArrayList<>();
        for (DataSourceInfo item : dataSources) {
            String errorMsg = toolService.testConnection(item.getDriverClassName(), item.getJdbcUrl(), item.getUserName(), item.getPassword());
            resultList.add(ConnectionTestResult.builder().dbName(item.getDbName()).errorMsg(errorMsg).build());
        }
        return resultList;
    }

    private DataSourceInfo toDataSource(DataSourceSaveParam param) {
        return DataSourceInfo.builder()
                .dbName(param.getDbName())
                .driverClassName(param.getDriverClassName())
                .jdbcUrl(param.getJdbcUrl())
                .userName(param.getUserName())
                .password(param.getPassword())
                .remark(param.getRemark())
                .tenantId(injectTenantId())
                .build();
    }

}
