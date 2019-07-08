package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class JobInstanceIdParam implements CommonParam {

    protected String id;

    @Override
    public String validate() {
        if (StringUtils.isBlank(id)) {
            return "6200";
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public JobInstanceIdParam setId(String id) {
        this.id = id;
        return this;
    }
}
