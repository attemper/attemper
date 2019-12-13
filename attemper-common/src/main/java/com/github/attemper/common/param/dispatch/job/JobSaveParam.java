package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.enums.JobStatus;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class JobSaveParam extends JobContentSaveParam {

    protected String displayName;

    protected Integer status;

    protected int concurrent;

    protected int once;

    protected String remark;

    public String validate() {
        if (StringUtils.isBlank(displayName)){
            return "6003";
        }
        if (JobStatus.get(status) == null) {
            return "6004";
        }
        return super.validate();
    }

    public String getDisplayName() {
        return displayName;
    }

    public JobSaveParam setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public JobSaveParam setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public JobSaveParam setConcurrent(int concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    public int getOnce() {
        return once;
    }

    public JobSaveParam setOnce(int once) {
        this.once = once;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public JobSaveParam setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
