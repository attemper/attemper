package com.github.attemper.executor.ext.param;

public class SqlParam {

    protected String dbName;

    protected String sql;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getDbName() != null) {
            sb.append("dbName(" + getDbName() + ") ");
        }
        if (getSql() != null) {
            sb.append("sql(" + getSql() + ") ");
        }
        return sb.toString();
    }
}
