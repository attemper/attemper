package com.github.attemper.common.param.dispatch.datasource;

import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceNamesParam implements CommonParam {

    protected List<String> dbNames;

    public String validate() {
        if (dbNames == null || dbNames.isEmpty()) {
            return "7100";
        }
        return null;
    }
}
