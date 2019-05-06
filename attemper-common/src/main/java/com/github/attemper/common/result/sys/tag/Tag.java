package com.github.attemper.common.result.sys.tag;

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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag{

	protected String tagName;

	protected String displayName;

	protected Integer tagType;

	protected Date createTime;

	protected Date updateTime;

	protected String remark;

	protected String tenantId;

}