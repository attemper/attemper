package com.sse.attemper.core.ext.job.factory;

import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.job.ExecutableJob;
import com.sse.attemper.sdk.common.exception.RTException;
import org.quartz.*;

public class JobDetailFactory {

    public static JobDetail getJobDetail(String jobName, String tenantId) {
        Scheduler scheduler = ContextBeanAware.getBean(Scheduler.class);
        JobDetail jobDetail = null;
        try {
            jobDetail = scheduler.getJobDetail(new JobKey(jobName, tenantId));
        } catch (SchedulerException e) {
            throw new RTException(500, e);
        }
        return jobDetail != null ? jobDetail : JobBuilder.newJob(ExecutableJob.class)
                .withIdentity(jobName, tenantId)
                .build();
    }
}
