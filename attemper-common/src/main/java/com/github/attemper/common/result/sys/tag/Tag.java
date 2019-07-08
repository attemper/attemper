package com.github.attemper.common.result.sys.tag;

import lombok.*;

import java.util.Date;

@ToString
public class Tag{

	protected String tagName;

	protected String displayName;

	protected Integer tagType;

	protected Date createTime;

	protected Date updateTime;

	protected String remark;

	public String getTagName() {
		return tagName;
	}

	public Tag setTagName(String tagName) {
		this.tagName = tagName;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Tag setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public Integer getTagType() {
		return tagType;
	}

	public Tag setTagType(Integer tagType) {
		this.tagType = tagType;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Tag setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Tag setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public Tag setRemark(String remark) {
		this.remark = remark;
		return this;
	}
}