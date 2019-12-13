package com.github.attemper.core.ext.condition;

import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.condition.sub.CommonConditionResult;

import java.util.Map;

public interface ConditionStrategy<T> {

    boolean validate(Condition condition, Map<String, Object> variableMap);

    <T extends CommonConditionResult> T  parse(Condition condition);
}
