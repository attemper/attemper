package com.github.attemper.common.param.dispatch.condition.sub;

import com.github.attemper.common.enums.ConditionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FtpFileConditionParam extends LocalFileConditionParam {

    protected String prefix;

    public FtpFileConditionParam() {
        this.conditionType = ConditionType.FTP_FILE.getValue();
    }

    @Override
    public String validate() {
        return super.validate();
    }
}
