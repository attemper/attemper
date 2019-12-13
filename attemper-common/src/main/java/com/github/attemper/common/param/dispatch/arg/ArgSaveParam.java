package com.github.attemper.common.param.dispatch.arg;

import com.github.attemper.common.enums.ArgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArgSaveParam extends ArgNameParam {

    protected Integer argType;

    protected String argValue;

    protected Integer genericType;

    protected String attribute;

    protected String remark;

    public String validate() {
        if (ArgType.get(argType) == null) {
            return "7001";
        }
        return super.validate();
    }

}
