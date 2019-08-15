package com.github.attemper.common.result.app.gist;

import lombok.ToString;

import java.util.Date;

@ToString
public class Gist {

    protected String gistName;

    protected Date createTime;

    protected String tenantId;

    public String getGistName() {
        return gistName;
    }

    public Gist setGistName(String gistName) {
        this.gistName = gistName;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Gist setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Gist setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
