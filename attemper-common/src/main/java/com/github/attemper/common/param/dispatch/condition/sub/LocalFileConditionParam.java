package com.github.attemper.common.param.dispatch.condition.sub;

import com.github.attemper.common.enums.ConditionType;
import com.github.attemper.java.sdk.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LocalFileConditionParam extends ConditionCommonParam {

    protected String filePath;

    protected String fileName;

    public LocalFileConditionParam() {
        this.conditionType = ConditionType.LOCAL_FILE.getValue();
    }

    @Override
    public String validate() {
        if (StringUtils.isBlank(filePath)) {
            return "6420";
        }
        if (StringUtils.isBlank(fileName)) {
            return "6421";
        }
        return super.validate();
    }
}
