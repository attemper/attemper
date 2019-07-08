package com.github.attemper.common.result.dispatch.datasource;

import lombok.ToString;

@ToString
public class DataSourceInfo {

    protected String dbName;

    protected String driverClassName;

    protected String jdbcUrl;

    protected String userName;

    protected String password;

    protected String remark;

    protected String tenantId;

    public String getDbName() {
        return dbName;
    }

    public DataSourceInfo setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public DataSourceInfo setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public DataSourceInfo setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public DataSourceInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DataSourceInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DataSourceInfo setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public DataSourceInfo setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
