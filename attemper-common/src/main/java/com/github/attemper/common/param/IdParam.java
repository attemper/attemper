package com.github.attemper.common.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class IdParam implements CommonParam {

    protected String id;

    public String validate() {
        if (StringUtils.isBlank(id)) {
            return "1504";
        }
        return null;
    }
}
