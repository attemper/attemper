package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class JobNameParam implements CommonParam {

    protected String jobName;

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }

    public String getJobName() {
        return jobName;
    }

    public JobNameParam setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }
}
