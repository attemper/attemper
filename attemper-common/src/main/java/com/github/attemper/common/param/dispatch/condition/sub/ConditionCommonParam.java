package com.github.attemper.common.param.dispatch.condition.sub;

import com.github.attemper.common.param.dispatch.condition.ConditionNameParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConditionCommonParam extends ConditionNameParam {

    protected int conditionType;

}
