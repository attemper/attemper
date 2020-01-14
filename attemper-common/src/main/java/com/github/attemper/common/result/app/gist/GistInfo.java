package com.github.attemper.common.result.app.gist;

import lombok.ToString;

@ToString
public class GistInfo {

    protected String id;

    protected String gistName;

    protected String version;

    protected long updateTime;

    protected String content;

    protected String tenantId;

    public String getId() {
        return id;
    }

    public GistInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getGistName() {
        return gistName;
    }

    public GistInfo setGistName(String gistName) {
        this.gistName = gistName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public GistInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public GistInfo setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public GistInfo setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public GistInfo setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
