package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataSourceNamesParam implements CommonParam {

    protected List<String> dbNames;

    public String validate() {
        if (dbNames == null || dbNames.isEmpty()) {
            return "7100";
        }
        return null;
    }
}
