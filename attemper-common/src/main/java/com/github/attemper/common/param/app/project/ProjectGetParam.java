package com.github.attemper.common.param.app.project;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class ProjectGetParam implements CommonParam {

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

    public ProjectGetParam setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
}
