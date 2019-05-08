package com.github.attemper.config.scheduler.job;

import com.github.attemper.common.result.CommonResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.scheduler.service.JobCallingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecutableJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobCallingService jobCallingService = SpringContextAware.getBean(JobCallingService.class);
        CommonResult<Void> commonResult = jobCallingService.invoke(
                context.getJobDetail().getKey().getName(),
                context.getTrigger().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
        System.out.println(commonResult);
    }
}
