package com.thor.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 标签<br>
 * 平铺维度：角色、用户组、岗位等
 * @author ldang
 */
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tag{

	@ApiModelProperty(value = "标签名称", dataType = "String", position = 1)
	private String tagName;

	@ApiModelProperty(value = "标签中文名", dataType = "String", position = 10)
	private String displayName;

	@ApiModelProperty(value = "标签类型", dataType = "Integer", position = 15)
	private Integer tagType;

	@ApiModelProperty(value = "创建日期", dataType = "Date", position = 20)
	private Date createTime;

	@ApiModelProperty(value = "上次修改日期", dataType = "Date", position = 21)
	private Date updateTime;

	@ApiModelProperty(value = "备注", dataType = "String", position = 25)
	private String remark;

	@ApiModelProperty(value = "租户编号", dataType = "String", position = 30)
	private String tenantId;
}