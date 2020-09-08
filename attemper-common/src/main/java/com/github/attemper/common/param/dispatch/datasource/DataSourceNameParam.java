package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class DataSourceNameParam implements CommonParam {

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

    public DataSourceNameParam setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }
}
