package com.github.attemper.common.result.app.program;

import lombok.ToString;

@ToString
public class ProgramPackage {

    protected String id;

    protected String programName;

    protected String packageName;

    protected Long uploadTime;

    protected Long loadTime;

    protected Long unloadTime;

    protected byte[] content;

    protected String tenantId;

    public String getId() {
        return id;
    }

    public ProgramPackage setId(String id) {
        this.id = id;
        return this;
    }

    public String getProgramName() {
        return programName;
    }

    public ProgramPackage setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public ProgramPackage setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public ProgramPackage setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public Long getLoadTime() {
        return loadTime;
    }

    public ProgramPackage setLoadTime(Long loadTime) {
        this.loadTime = loadTime;
        return this;
    }

    public Long getUnloadTime() {
        return unloadTime;
    }

    public ProgramPackage setUnloadTime(Long unloadTime) {
        this.unloadTime = unloadTime;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public ProgramPackage setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public ProgramPackage setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
