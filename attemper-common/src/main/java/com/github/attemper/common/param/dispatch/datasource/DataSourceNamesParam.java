package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;

import java.util.List;

@ToString
public class DataSourceNamesParam implements CommonParam {

    protected List<String> dbNames;

    public String validate() {
        if (dbNames == null || dbNames.isEmpty()) {
            return "7100";
        }
        return null;
    }

    public List<String> getDbNames() {
        return dbNames;
    }

    public DataSourceNamesParam setDbNames(List<String> dbNames) {
        this.dbNames = dbNames;
        return this;
    }
}
