package com.github.attemper.common.param.dispatch.instance;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class InstanceActParam implements CommonParam {

    protected String procInstId;

    protected String actId;

    @Override
    public String validate() {
        if (StringUtils.isBlank(procInstId)) {
            return "6200";
        }
        return null;
    }
}
