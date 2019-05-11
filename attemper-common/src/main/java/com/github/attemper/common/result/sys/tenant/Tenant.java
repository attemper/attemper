package com.github.attemper.common.result.sys.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * tenant
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant{

	protected String userName;

	protected String displayName;

	protected String password;

	protected String email;

	protected String mobile;

	protected Integer status;

	protected String sign;

	protected Boolean admin;

	protected Date createTime;

	protected Date updateTime;

}
