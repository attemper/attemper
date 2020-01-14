package com.github.attemper.common.result.app.gist;

import lombok.ToString;

@ToString
public class Gist {

    protected String gistName;

    protected long createTime;

    protected String tenantId;

    public String getGistName() {
        return gistName;
    }

    public Gist setGistName(String gistName) {
        this.gistName = gistName;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Gist setCreateTime(long createTime) {
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
