package com.github.attemper.common.result.dispatch.datasource;

import lombok.*;

@ToString
public class ConnectionTestResult {

    protected String dbName;

    protected String errorMsg;

    public String getDbName() {
        return dbName;
    }

    public ConnectionTestResult setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ConnectionTestResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
