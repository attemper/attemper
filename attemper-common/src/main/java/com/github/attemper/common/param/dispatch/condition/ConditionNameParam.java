package com.github.attemper.common.param.dispatch.condition;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConditionNameParam implements CommonParam {

    protected String conditionName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(conditionName)) {
            return "6400";
        }
        return null;
    }
}
