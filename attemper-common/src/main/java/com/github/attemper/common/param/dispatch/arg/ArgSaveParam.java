package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.enums.ArgType;
import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class ArgSaveParam implements CommonParam {

    protected String argName;

    protected Integer argType;

    protected String argValue;

    protected Integer genericType;

    protected String attribute;

    protected String remark;

    public String validate() {
        if (StringUtils.isBlank(argName)) {
            return "7000";
        }
        if (ArgType.get(argType) == null) {
            return "7001";
        }
        return null;
    }

}
