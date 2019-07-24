package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.enums.JobStatus;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class JobSaveParam extends JobContentSaveParam {

    protected String displayName;

    protected Integer status;

    protected Boolean concurrent;

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

    @Override
    public void preHandle() {
        if (concurrent == null) {
            concurrent = false;
        }
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

    public Boolean getConcurrent() {
        return concurrent;
    }

    public JobSaveParam setConcurrent(Boolean concurrent) {
        this.concurrent = concurrent;
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
