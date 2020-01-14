package com.github.attemper.common.result.app.project;

import lombok.ToString;

@ToString
public class ProjectInfo {

    protected String projectName;

    protected String parentProjectName;

    protected String displayName;

    protected String uri;

    protected String contextPath;

    protected int uriType;

    protected String tenantId;

    public String getProjectName() {
        return projectName;
    }

    public ProjectInfo setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getParentProjectName() {
        return parentProjectName;
    }

    public ProjectInfo setParentProjectName(String parentProjectName) {
        this.parentProjectName = parentProjectName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ProjectInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public ProjectInfo setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getContextPath() {
        return contextPath;
    }

    public ProjectInfo setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public int getUriType() {
        return uriType;
    }

    public ProjectInfo setUriType(int uriType) {
        this.uriType = uriType;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public ProjectInfo setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
