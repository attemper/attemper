package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class JobSaveParam implements CommonParam {

    protected String jobName;

    protected String displayName;

    protected String jobContent;

    protected Integer status;

    protected Integer timeout;

    protected Boolean concurrent;

    protected String remark;

    public String validate() {
        if (StringUtils.isBlank(jobName)){
            return "6000";
        }
        if (StringUtils.isBlank(displayName)){
            return "6003";
        }
        if (JobStatus.get(status) == null) {
            return "6004";
        }
        return null;
    }

    @Override
    public void preHandle() {
        if (concurrent == null) {
            concurrent = false;
        }
    }

    public String getJobName() {
        return jobName;
    }

    public JobSaveParam setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public JobSaveParam setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getJobContent() {
        return jobContent;
    }

    public JobSaveParam setJobContent(String jobContent) {
        this.jobContent = jobContent;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public JobSaveParam setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public JobSaveParam setTimeout(Integer timeout) {
        this.timeout = timeout;
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
