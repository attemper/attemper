package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class JobContentSaveParam implements CommonParam {

    protected String jobName;

    protected String content;

    public String validate() {
        if (StringUtils.isBlank(jobName)){
            return "6000";
        }
        return null;
    }

    public String getJobName() {
        return jobName;
    }

    public JobContentSaveParam setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getContent() {
        return content;
    }

    public JobContentSaveParam setContent(String content) {
        this.content = content;
        return this;
    }

}
