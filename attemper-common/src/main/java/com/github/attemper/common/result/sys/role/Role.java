package com.github.attemper.common.result.sys.role;

import lombok.ToString;

@ToString
public class Role {

	protected String roleName;

	protected String displayName;

	protected String remark;

	public String getRoleName() {
		return roleName;
	}

	public Role setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Role setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public Role setRemark(String remark) {
		this.remark = remark;
		return this;
	}
}