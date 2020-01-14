package com.github.attemper.common.param.dispatch.condition.sub;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.ConditionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SqlConditionParam extends ConditionCommonParam {

    protected String dbName;

    protected String sql;

    public SqlConditionParam() {
        this.conditionType = ConditionType.SQL.getValue();
    }

    @Override
    public String validate() {
        if (sql == null || !sql.toLowerCase().startsWith(CommonConstants.select)) {
            return "7020";
        }
        return super.validate();
    }
}
