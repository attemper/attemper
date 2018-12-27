package com.thor.core.entity;

import com.thor.common.constant.GlobalConstants;
import com.thor.core.constant.SysDBConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 租户
 * @author ldang
 */
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = GlobalConstants.TABLE_PREFIX + SysDBConstants.TB_TENANT)
@Entity
public class Tenant{

	@ApiModelProperty(value = "租户编号", dataType = "String", position = 1)
	@Id
	@Column(name = "ID")
	private String id;

	@ApiModelProperty(value = "租户名称", dataType = "String", position = 5)
	@Column(name = "NAME")
	private String name;

	@ApiModelProperty(value = "静态签名", dataType = "String", position = 10)
	@Column(name = "SIGN")
	private String sign;

	@ApiModelProperty(value = "虚拟管理员账号", dataType = "String", position = 15)
	@Column(name = "ADMIN")
	private String admin;

	@ApiModelProperty(value = "创建时间", dataType = "Date", position = 20)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@ApiModelProperty(value = "最近更新时间", dataType = "Date", position = 25)
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
}
