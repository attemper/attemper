package com.github.attemper.common.param.dispatch.job;

import lombok.ToString;

import java.util.List;

@ToString
public class JobArgSaveParam extends JobNameParam {

    protected List<String> argNames;

    public List<String> getArgNames() {
        return argNames;
    }

    public JobArgSaveParam setArgNames(List<String> argNames) {
        this.argNames = argNames;
        return this;
    }
}
