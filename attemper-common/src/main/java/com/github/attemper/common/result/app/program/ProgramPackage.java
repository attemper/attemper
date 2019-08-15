package com.github.attemper.common.result.app.program;

import lombok.ToString;

import java.util.Date;

@ToString
public class ProgramPackage {

    protected String id;

    protected String programName;

    protected String packageName;

    protected Date uploadTime;

    protected Date loadTime;

    protected Date unloadTime;

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

    public Date getUploadTime() {
        return uploadTime;
    }

    public ProgramPackage setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public ProgramPackage setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
        return this;
    }

    public Date getUnloadTime() {
        return unloadTime;
    }

    public ProgramPackage setUnloadTime(Date unloadTime) {
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
