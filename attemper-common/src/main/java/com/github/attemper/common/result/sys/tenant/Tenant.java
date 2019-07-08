package com.github.attemper.common.result.sys.tenant;

import lombok.*;

import java.util.Date;

@ToString
public class Tenant{

	protected String userName;

	protected String displayName;

	protected String password;

	protected String email;

	protected String mobile;

	protected Integer status;

	protected Boolean admin;

	protected Date createTime;

	protected Date updateTime;

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

	public Integer getStatus() {
		return status;
	}

	public Tenant setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public Tenant setAdmin(Boolean admin) {
		this.admin = admin;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Tenant setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Tenant setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
