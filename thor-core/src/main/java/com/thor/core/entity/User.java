package com.thor.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

/**
 * 用户
 * @auth ldang
 */
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{

	@ApiModelProperty(value = "用户名", dataType = "String", position = 1)
	private String userName;

	@ApiModelProperty(value = "中文名或英文全称", dataType = "String", position = 5)
	private String displayName;

	@ApiModelProperty(value = "密码", dataType = "String", position = 10)
	private String password;

	@ApiModelProperty(value = "邮箱号", dataType = "String", position = 15)
	private String email;

	@ApiModelProperty(value = "手机号", dataType = "String", position = 20)
	private String mobile;

	@ApiModelProperty(value = "状态", dataType = "Integer", position = 25)
	private Integer status;

	@ApiModelProperty(value = "创建时间", dataType = "Date", position = 26)
	private Date createTime;

	@ApiModelProperty(value = "最近更新时间", dataType = "Date", position = 27)
	private Date updateTime;

	@ApiModelProperty(value = "租户编号", dataType = "String", position = 30)
	private String tenantId;

	@Override
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof User)) {
	        return false;
        }
        User user = (User) o;
		return (Objects.equals(userName, user.userName)
                || Objects.equals(email, user.email)
                || Objects.equals(mobile, user.mobile)) &&
				Objects.equals(password, user.password) &&
				Objects.equals(status, user.status) &&
				Objects.equals(tenantId, user.tenantId);
	}
}
