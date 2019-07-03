package com.github.attemper.common.param.dispatch.trigger;

import com.github.attemper.common.param.CommonParam;
import org.apache.commons.lang.StringUtils;

public class TriggerGetParam implements CommonParam {

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

    public TriggerGetParam setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }
}
