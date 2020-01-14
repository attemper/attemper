package com.github.attemper.common.result.dispatch.condition.sub;

import lombok.ToString;

@ToString
public class CommonConditionResult {

    protected String conditionName;

    protected String tenantId;

    public String getConditionName() {
        return conditionName;
    }

    public CommonConditionResult setConditionName(String conditionName) {
        this.conditionName = conditionName;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public CommonConditionResult setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
