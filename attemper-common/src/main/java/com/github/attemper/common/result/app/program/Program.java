package com.github.attemper.common.result.app.program;

import lombok.ToString;

@ToString
public class Program {

    protected String programName;

    protected int injectOrder;

    protected long createTime;

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

    public long getCreateTime() {
        return createTime;
    }

    public Program setCreateTime(long createTime) {
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
