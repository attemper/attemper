package com.github.attemper.common.result.dispatch.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceInfo {

    protected String dbName;

    protected String driverClassName;

    protected String jdbcUrl;

    protected String userName;

    protected String password;

    protected String remark;

    protected String tenantId;
}
