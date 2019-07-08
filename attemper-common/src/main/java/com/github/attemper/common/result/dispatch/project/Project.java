package com.github.attemper.common.result.dispatch.project;

import lombok.ToString;

import java.util.Date;

/**
 * The project may be many to one of tenant
 * @author ldang
 */
@ToString
public class Project {

	protected String projectName;

	protected String parentProjectName;

	protected String displayName;

	protected String contextPath;

	protected boolean bindExecutor;

    protected Integer position;

	protected Date createTime;

	protected Date updateTime;

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

	public boolean isBindExecutor() {
		return bindExecutor;
	}

	public Project setBindExecutor(boolean bindExecutor) {
		this.bindExecutor = bindExecutor;
		return this;
	}

	public Integer getPosition() {
		return position;
	}

	public Project setPosition(Integer position) {
		this.position = position;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Project setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Project setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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