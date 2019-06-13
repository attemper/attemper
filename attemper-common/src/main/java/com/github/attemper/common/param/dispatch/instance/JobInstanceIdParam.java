package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class JobInstanceIdParam implements CommonParam {

    protected String id;

    @Override
    public String validate() {
        if (StringUtils.isBlank(id)) {
            return "6200";
        }
        return null;
    }
}
