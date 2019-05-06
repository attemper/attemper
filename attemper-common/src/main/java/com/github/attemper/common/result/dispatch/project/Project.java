package com.github.attemper.common.result.dispatch.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The project is many to one of tenant
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

	protected String projectName;

	protected String parentProjectName;

	protected String displayName;

	protected String contextPath;

    protected Integer position;

	protected Date createTime;

	protected Date updateTime;

	protected String tenantId;

}