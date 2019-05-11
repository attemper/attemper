package com.github.attemper.common.result.sys.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资源<br>
 * 支持父子菜单、按钮、区块、物料等<br>
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

	protected String resourceName;

	protected String parentResourceName;

	protected String displayName;

	protected Integer resourceType;

	protected String uri;

	protected String icon;

    protected Integer position;

	protected Date createTime;

	protected Date updateTime;

	protected String extAttr;

}