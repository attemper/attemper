package com.github.attemper.invoker.job;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.invoker.service.JobCallingService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.Map;

public class ExecutableJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobCallingService jobCallingService = SpringContextAware.getBean(JobCallingService.class);
        IdGenerator idGenerator = SpringContextAware.getBean(IdGenerator.class);
        JobDetail jobDetail = context.getJobDetail();
        String jobName;
        Map<String, Object> dataMap = null;
        if (!jobDetail.getJobDataMap().containsKey(CommonConstants.jobName)) {
            jobName = jobDetail.getKey().getName();
        } else {
            jobName = jobDetail.getJobDataMap().getString(CommonConstants.jobName);
            dataMap = new HashMap<>();
            dataMap.put(CommonConstants.delayId, jobDetail.getKey().getName());
        }
        jobCallingService.execute(
                idGenerator.getNextId(),
                jobName,
                context.getTrigger().getKey().getName(),
                context.getJobDetail().getKey().getGroup(),
                dataMap);
    }
}
