package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class DataSourceGetParam implements CommonParam {

    protected String dbName;

    public String validate() {
        if (StringUtils.isBlank(dbName)) {
            return "7100";
        }
        return null;
    }

    public String getDbName() {
        return dbName;
    }

    public DataSourceGetParam setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }
}
