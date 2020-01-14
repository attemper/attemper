package com.github.attemper.common.param.dispatch.condition;

import com.github.attemper.common.param.dispatch.condition.sub.FtpFileConditionParam;
import com.github.attemper.common.param.dispatch.condition.sub.LocalFileConditionParam;
import com.github.attemper.common.param.dispatch.condition.sub.SqlConditionParam;
import com.github.attemper.common.param.dispatch.job.JobNameParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ConditionSaveParam extends JobNameParam {

    protected List<FtpFileConditionParam> ftpFileConditions;

    protected List<LocalFileConditionParam> localFileConditions;

    protected List<SqlConditionParam> sqlConditions;

    public ConditionSaveParam() {
    }

    public String validate() {
        String code = validaFieldParam(this.ftpFileConditions);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.localFileConditions);
        if (code != null) {
            return code;
        }
        code = validaFieldParam(this.sqlConditions);
        if (code != null) {
            return code;
        }
        return super.validate();
    }

    private String validaFieldParam(List<? extends ConditionNameParam> list) {
        if (list != null) {
            for (ConditionNameParam item : list) {
                item.preHandle();
                String code = item.validate();
                if (item.validate() != null) {
                    return code;
                }
            }
        }
        return null;
    }
}
