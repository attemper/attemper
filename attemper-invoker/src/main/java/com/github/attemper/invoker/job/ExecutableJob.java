package com.github.attemper.invoker.job;

import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.invoker.service.JobCallingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecutableJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobCallingService jobCallingService = SpringContextAware.getBean(JobCallingService.class);
        jobCallingService.execute(
                context.getJobDetail().getKey().getName(),
                context.getTrigger().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
    }
}
