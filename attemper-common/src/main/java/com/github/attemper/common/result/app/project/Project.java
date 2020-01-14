package com.github.attemper.common.result.app.project;

import lombok.ToString;

/**
 * The project may be many to one of tenant
 */
@ToString
public class Project {

	protected String projectName;

	protected String parentProjectName;

	protected String displayName;

	protected String contextPath;

	protected int bindExecutor;

	protected String tenantId;

	public String getProjectName() {
		return projectName;
	}

	public Project setProjectName(String projectName) {
		this.projectName = projectName;
		return this;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public Project setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Project setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getContextPath() {
		return contextPath;
	}

	public Project setContextPath(String contextPath) {
		this.contextPath = contextPath;
		return this;
	}

	public int getBindExecutor() {
		return bindExecutor;
	}

	public Project setBindExecutor(int bindExecutor) {
		this.bindExecutor = bindExecutor;
		return this;
	}

	public String getTenantId() {
		return tenantId;
	}

	public Project setTenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}
}