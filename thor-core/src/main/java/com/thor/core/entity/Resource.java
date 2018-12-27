package com.thor.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resource {

	@ApiModelProperty(value = "资源名称", dataType = "String", position = 1)
	private String resourceName;

	@ApiModelProperty(value = "父资源名称", dataType = "String", position = 5)
	private String parentResourceName;

	@ApiModelProperty(value = "中文名称", dataType = "String", position = 10)
	private String displayName;

	@ApiModelProperty(value = "资源类型", dataType = "Integer", position = 15)
	private Integer resourceType;

	@ApiModelProperty(value = "资源路径", dataType = "String", position = 20)
	private String uri;

	@ApiModelProperty(value = "图标代码", dataType = "String", position = 21)
	private String icon;

    @ApiModelProperty(value = "位置/排序", dataType = "Integer", position = 23)
    private Integer position;

	@ApiModelProperty(value = "创建时间", dataType = "Date", position = 25)
	private Date createTime;

	@ApiModelProperty(value = "最近更新时间", dataType = "Date", position = 30)
	private Date updateTime;

	@ApiModelProperty(value = "额外属性(json字符串存储)", dataType = "String", position = 35)
	private String extAttr;

	@ApiModelProperty(value = "租户编号", dataType = "String", position = 40)
	private String tenantId;
}