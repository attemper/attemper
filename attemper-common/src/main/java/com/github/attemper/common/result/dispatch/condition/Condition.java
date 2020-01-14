package com.github.attemper.common.result.dispatch.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Condition {

    protected String conditionName;

    protected int conditionType;

    protected String content;

    private String tenantId;
}
