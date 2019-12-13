package com.github.attemper.common.result.sys.tenant;

import lombok.ToString;

@ToString
public class Tenant{

	protected String userName;

	protected String displayName;

	protected String password;

	protected String email;

	protected String mobile;

	protected int status;

	protected int superAdmin;

	protected String sendConfig;

	public String getUserName() {
		return userName;
	}

	public Tenant setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Tenant setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Tenant setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Tenant setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	public Tenant setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Tenant setStatus(int status) {
		this.status = status;
		return this;
	}

	public int getSuperAdmin() {
		return superAdmin;
	}

	public Tenant setSuperAdmin(int superAdmin) {
		this.superAdmin = superAdmin;
		return this;
	}

	public String getSendConfig() {
		return sendConfig;
	}

	public Tenant setSendConfig(String sendConfig) {
		this.sendConfig = sendConfig;
		return this;
	}
}
