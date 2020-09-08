package com.github.attemper.common.param.dispatch.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
public class DataSourceSaveParam extends DataSourceNameParam {

    protected String driverClassName;

    protected String jdbcUrl;

    protected String userName;

    protected String password;

    protected String attribute;

    public String validate() {
        if (StringUtils.isBlank(jdbcUrl)) {
            return "7101";
        }
        return super.validate();
    }

}
