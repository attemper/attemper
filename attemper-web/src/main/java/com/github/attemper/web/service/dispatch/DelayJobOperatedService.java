package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.delay.DelayJobIdParam;
import com.github.attemper.common.param.dispatch.delay.DelayJobListParam;
import com.github.attemper.common.param.dispatch.job.JobNameParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerSaveParam;
import com.github.attemper.common.param.dispatch.trigger.sub.DailyTimeIntervalTriggerParam;
import com.github.attemper.common.result.dispatch.delay.DelayJob;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.core.dao.dispatch.DelayJobMapper;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.core.service.dispatch.TriggerService;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.java.sdk.common.web.enums.DelayJobStatus;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobExtSaveParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobIdsParam;
import com.github.attemper.java.sdk.common.web.result.delay.DelayJobResult;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.quartz.DateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DelayJobOperatedService extends BaseServiceAdapter {

    @Autowired
    private DelayJobMapper mapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private TriggerOperatedService triggerOperatedService;

    @Autowired
    private IdGenerator idGenerator;

    public Map<String, Object> list(DelayJobListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<DelayJob> list = (Page<DelayJob>) mapper.list(paramMap);
        list.forEach(item -> setNextFireTime(item));
        return PageUtil.toResultMap(list);
    }

    private void setNextFireTime(DelayJob delayJob) {
        List<Date> allTriggerDates = new ArrayList<>();
        TriggerResult triggerResult = triggerService.get(new TriggerGetParam().setJobName(delayJob.getId()));
        allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getDailyIntervalTriggers(), injectTenantId()));
        if (allTriggerDates.size() > 0) {
            Collections.sort(allTriggerDates);
            delayJob.setNextFireTimes(allTriggerDates);
        }
    }

    public DelayJob get(DelayJobIdParam param) {
        return mapper.get(injectTenantIdToMap(param));
    }

    public DelayJobResult add(DelayJobExtSaveParam param) {
        if (StringUtils.isBlank(param.getId())) {
            param.setId(idGenerator.getNextId());
        } else {
            DelayJob delayJob = get(new DelayJobIdParam().setId(param.getId()));
            if (delayJob != null) {
                throw new DuplicateKeyException(param.getId());
            }
        }
        validateJob(param.getJobName());
        DelayJob delayJob = toDelayJob(param);
        mapper.add(delayJob);
        TriggerSaveParam triggerSaveParam = buildTriggerParam(param);
        triggerOperatedService.update(triggerSaveParam);
        return null;
    }

    public Void remove(DelayJobIdsParam param) {
        return null;
    }

    private DelayJob toDelayJob(DelayJobExtSaveParam param) {
        return new DelayJob()
                .setId(param.getId())
                .setJobName(param.getJobName())
                .setRequestTime(new Date())
                .setStatus(DelayJobStatus.VALID.getStatus())
                .setTenantId(injectTenantId());
    }

    private void validateJob(String jobName) {
        Job job = jobService.get(new JobNameParam().setJobName(jobName));
        if (job == null) {
            throw new RTException(6050, jobName);
        }
    }

    private TriggerSaveParam buildTriggerParam(DelayJobExtSaveParam param) {
        TriggerSaveParam triggerSaveParam = new TriggerSaveParam();
        triggerSaveParam.setJobName(param.getId());
        Map<String, Object> map = new HashMap<>();
        map.put(CommonConstants.jobName, param.getJobName());
        triggerSaveParam.setJobDataMap(map);

        DailyTimeIntervalTriggerParam dailyParam = new DailyTimeIntervalTriggerParam();
        dailyParam.setTriggerName(idGenerator.getNextId());
        dailyParam.setStartTime(param.getStartTime());
        dailyParam.setTimeUnit(DateBuilder.IntervalUnit.SECOND.name());
        dailyParam.setInterval(param.getInterval());
        dailyParam.setEndTime(param.getEndTime());
        dailyParam.setMisfireInstruction(param.getMisfireInstruction());
        dailyParam.setCalendarNames(param.getCalendarNames());
        List<DailyTimeIntervalTriggerParam> list = new ArrayList<>();
        list.add(dailyParam);
        triggerSaveParam.setDailyIntervalTriggers(list);

        return triggerSaveParam;
    }
}
