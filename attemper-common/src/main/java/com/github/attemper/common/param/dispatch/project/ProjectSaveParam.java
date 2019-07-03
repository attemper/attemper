package com.github.attemper.common.param.dispatch.project;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;

public class ProjectSaveParam implements CommonParam {

    protected String projectName;

    protected String parentProjectName;

    protected String displayName;

    protected String contextPath;

    protected boolean bindExecutor;

    protected Integer position;

    public String validate() {
        if (StringUtils.isBlank(projectName)) {
            return "6500";
        } else if (projectName.length() >= 255) {
            return "1503";
        }
        if (StringUtils.isBlank(displayName)) {
            return "6503";
        }
        return null;
    }

    public String getProjectName() {
        return projectName;
    }

    public ProjectSaveParam setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getParentProjectName() {
        return parentProjectName;
    }

    public ProjectSaveParam setParentProjectName(String parentProjectName) {
        this.parentProjectName = parentProjectName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ProjectSaveParam setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getContextPath() {
        return contextPath;
    }

    public ProjectSaveParam setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public boolean isBindExecutor() {
        return bindExecutor;
    }

    public ProjectSaveParam setBindExecutor(boolean bindExecutor) {
        this.bindExecutor = bindExecutor;
        return this;
    }

    public Integer getPosition() {
        return position;
    }

    public ProjectSaveParam setPosition(Integer position) {
        this.position = position;
        return this;
    }
}
