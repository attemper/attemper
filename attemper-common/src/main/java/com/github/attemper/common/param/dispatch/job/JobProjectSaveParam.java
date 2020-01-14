package com.github.attemper.common.param.dispatch.job;

import lombok.ToString;

@ToString
public class JobProjectSaveParam extends JobNameParam {

    protected String projectName;

    public String getProjectName() {
        return projectName;
    }

    public JobProjectSaveParam setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
}
