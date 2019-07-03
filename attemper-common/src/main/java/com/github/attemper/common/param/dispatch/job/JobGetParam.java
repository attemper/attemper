package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;

public class JobGetParam implements CommonParam {

    protected String jobName;

    protected Integer reversion;

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }

    public String getJobName() {
        return jobName;
    }

    public JobGetParam setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public Integer getReversion() {
        return reversion;
    }

    public JobGetParam setReversion(Integer reversion) {
        this.reversion = reversion;
        return this;
    }
}
