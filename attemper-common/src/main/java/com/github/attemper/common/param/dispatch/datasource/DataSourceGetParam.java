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
public class DataSourceGetParam implements CommonParam {

    protected String dbName;

    public String validate() {
        if (StringUtils.isBlank(dbName)) {
            return "7100";
        }
        return null;
    }

}
