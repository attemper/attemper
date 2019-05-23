package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceSaveParam implements CommonParam {

    protected String dbName;

    protected String driverClassName;

    protected String jdbcUrl;

    protected String userName;

    protected String password;

    protected String remark;

    public String validate() {
        if (StringUtils.isBlank(dbName)) {
            return "7100";
        }
        if (StringUtils.isBlank(jdbcUrl)) {
            return "7101";
        }
        return null;
    }

}
