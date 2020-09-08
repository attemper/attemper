package com.github.attemper.common.param;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class IdParam implements CommonParam {

    protected String id;

    public String validate() {
        if (StringUtils.isBlank(id)) {
            return "1504";
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public IdParam setId(String id) {
        this.id = id;
        return this;
    }
}
