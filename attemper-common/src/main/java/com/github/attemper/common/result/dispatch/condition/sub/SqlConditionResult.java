package com.github.attemper.common.result.dispatch.condition.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SqlConditionResult extends CommonConditionResult {

    protected String dbName;

    protected String sql;
}
