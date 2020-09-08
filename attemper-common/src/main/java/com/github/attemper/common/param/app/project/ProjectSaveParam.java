package com.github.attemper.common.param.app.project;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class ProjectSaveParam extends ProjectNameParam {

    protected String parentProjectName;

    protected String displayName;

    protected String contextPath;

    protected int bindExecutor;

    public String validate() {
        if (projectName.length() >= 255) {
            return "1503";
        }
        if (StringUtils.isBlank(displayName)) {
            return "6503";
        }
        return super.validate();
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

    public int getBindExecutor() {
        return bindExecutor;
    }

    public ProjectSaveParam setBindExecutor(int bindExecutor) {
        this.bindExecutor = bindExecutor;
        return this;
    }
}
