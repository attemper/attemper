package com.github.attemper.common.param.dispatch.job;

import lombok.ToString;

import java.util.List;

@ToString
public class JobConditionSaveParam extends JobNameParam {
    
    protected List<String> conditionNames;

    public List<String> getConditionNames() {
        return conditionNames;
    }

    public JobConditionSaveParam setConditionNames(List<String> conditionNames) {
        this.conditionNames = conditionNames;
        return this;
    }
}
