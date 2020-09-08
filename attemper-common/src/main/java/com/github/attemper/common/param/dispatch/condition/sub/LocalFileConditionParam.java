package com.github.attemper.common.param.dispatch.condition.sub;

import com.github.attemper.common.enums.ConditionType;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isBlank(fileName)) {
            return "6421";
        }
        return super.validate();
    }
}
