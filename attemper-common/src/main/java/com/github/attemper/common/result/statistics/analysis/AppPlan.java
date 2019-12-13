package com.github.attemper.common.result.statistics.analysis;

import lombok.ToString;

@ToString
public class AppPlan {

    protected String jobName;

    protected Long nextFireTime;

    public String getJobName() {
        return jobName;
    }

    public AppPlan setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public Long getNextFireTime() {
        return nextFireTime;
    }

    public AppPlan setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
        return this;
    }
}
