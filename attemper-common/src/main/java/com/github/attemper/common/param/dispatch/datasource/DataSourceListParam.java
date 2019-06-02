package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.PageSortParam;
import lombok.Data;

@Data
public class DataSourceListParam extends PageSortParam {

    protected String dbName;

    protected String driverClassName;

    protected String jdbcUrl;

    protected String userName;

    protected String remark;
}
