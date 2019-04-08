package com.sse.attemper.common.result.sys.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant{

	protected String id;

	protected String name;

	protected String sign;

	protected String admin;

	protected Date createTime;

	protected Date updateTime;

}
