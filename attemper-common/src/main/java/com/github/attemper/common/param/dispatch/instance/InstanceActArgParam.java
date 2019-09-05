package com.github.attemper.common.param.dispatch.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Setter
@Getter
@ToString
public class InstanceActArgParam extends InstanceIdParam {

    protected String actInstId;

    @Override
    public String validate() {
        if (StringUtils.isBlank(actInstId)) {
            return "6203";
        }
        return super.validate();
    }
}
