package com.github.attemper.common.param.app.project;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class ProjectNameParam implements CommonParam {

    protected String projectName;

    @Override
    public String validate() {
        if(StringUtils.isBlank(projectName)) {
            return "6500";
        }
        return null;
    }

    public String getProjectName() {
        return projectName;
    }

    public ProjectNameParam setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
}
