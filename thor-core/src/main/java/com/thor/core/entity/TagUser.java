package com.thor.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签与用户的关系
 * @author ldang
 */
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagUser {

	@ApiModelProperty(value = "标签标识", dataType = "String", position = 5)
	private String tagName;

	@ApiModelProperty(value = "标签类型", dataType = "String", position = 7)
	private String tagType;

	@ApiModelProperty(value = "用户名", dataType = "String", position = 1)
	private String userName;

	@ApiModelProperty(value = "租户编号", dataType = "String", position = 10)
	private String tenantId;
}