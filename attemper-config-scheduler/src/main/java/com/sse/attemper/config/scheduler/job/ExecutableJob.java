package com.sse.attemper.config.scheduler.job;

import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.scheduler.service.JobCallingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExecutableJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobCallingService jobCallingService = ContextBeanAware.getBean(JobCallingService.class);
        CommonResult<Void> commonResult = jobCallingService.invoke(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS").format(LocalDateTime.now()),
                context.getJobDetail().getKey().getName(),
                context.getTrigger().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
        System.out.println(commonResult);
    }
}
