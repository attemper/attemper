package com.github.attemper.common.result.app.program;

import lombok.ToString;

import java.util.Date;

@ToString
public class Program {

    protected String programName;

    protected int injectOrder;

    protected Date createTime;

    protected String tenantId;

    public String getProgramName() {
        return programName;
    }

    public Program setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    public int getInjectOrder() {
        return injectOrder;
    }

    public Program setInjectOrder(int injectOrder) {
        this.injectOrder = injectOrder;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Program setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Program setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
