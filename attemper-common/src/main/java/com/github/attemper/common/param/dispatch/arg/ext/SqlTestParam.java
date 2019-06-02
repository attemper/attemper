package com.github.attemper.common.param.dispatch.arg.ext;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;

@Data
public class SqlTestParam implements CommonParam {

    protected String dbName;

    protected String sql;

    @Override
    public String validate() {
        if (sql == null || !sql.toLowerCase().startsWith("select ")) {
            return "7020";
        }
        return null;
    }
}
