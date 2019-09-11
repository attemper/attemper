package com.github.attemper.common.result.statistics.analysis;

import lombok.ToString;

import java.util.Date;

@ToString
public class AppPlan {

    protected String jobName;

    protected Date prevFireTime;

    protected Date nextFireTime;

    public String getJobName() {
        return jobName;
    }

    public AppPlan setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public Date getPrevFireTime() {
        return prevFireTime;
    }

    public AppPlan setPrevFireTime(Date prevFireTime) {
        this.prevFireTime = prevFireTime;
        return this;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public AppPlan setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
        return this;
    }
}
