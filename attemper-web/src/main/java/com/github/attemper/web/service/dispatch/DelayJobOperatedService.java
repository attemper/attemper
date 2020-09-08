package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.delay.DelayJobIdParam;
import com.github.attemper.common.param.dispatch.delay.DelayJobListParam;
import com.github.attemper.common.param.dispatch.job.JobNameParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerWrapper;
import com.github.attemper.common.param.dispatch.trigger.sub.DailyTimeIntervalTriggerWrapper;
import com.github.attemper.common.result.dispatch.delay.DelayJob;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.core.dao.dispatch.DelayJobMapper;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.java.sdk.common.web.enums.DelayJobStatus;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobIdsParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobParam;
import com.github.attemper.java.sdk.common.web.result.delay.DelayJobResult;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
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
    private JobOperatedService jobOperatedService;

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
        List<Date> allTriggerDates = new ArrayList<>(10);
        TriggerWrapper triggerResult = jobOperatedService.getTrigger(new JobNameParam().setJobName(delayJob.getId()));
        if (triggerResult != null) {
            allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getDailyTimeIntervalTriggers(), injectTenantId()));
            if (allTriggerDates.size() > 0) {
                Collections.sort(allTriggerDates);
                delayJob.setNextFireTimes(allTriggerDates.size() <= 10 ? allTriggerDates : allTriggerDates.subList(0, 10));
            }
        }
    }

    public DelayJob get(DelayJobIdParam param) {
        return mapper.get(param.getId());
    }

    public DelayJobResult add(DelayJobParam param) {
        if (StringUtils.isBlank(param.getId())) {
            param.setId(idGenerator.getNextId());
        } else {
            DelayJob delayJob = get(new DelayJobIdParam().setId(param.getId()));
            if (delayJob != null) {
                throw new DuplicateKeyException(param.getId());
            }
        }
        Job job = jobOperatedService.validateAndGet(param.getJobName());// job exists?
        if (JobStatus.DISABLED.getStatus() == job.getStatus()) {
            throw new RTException(6300, param.getJobName());
        }
        jobOperatedService.validatePublished(param.getJobName()); // job was published?
        DelayJob delayJob = toDelayJob(param);
        mapper.add(delayJob);
        TriggerWrapper triggerWrapper = buildTriggerParam(param);
        jobOperatedService.updateTrigger(triggerWrapper);
        return new DelayJobResult().setId(param.getId());
    }

    public Void remove(DelayJobIdsParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        for (String id : param.getIds()) {
            TriggerWrapper triggerWrapper = new TriggerWrapper(id);
            jobOperatedService.updateTrigger(triggerWrapper);
        }
        mapper.delete(paramMap);
        return null;
    }

    private DelayJob toDelayJob(DelayJobParam param) {
        return new DelayJob()
                .setId(param.getId())
                .setJobName(param.getJobName())
                .setRequestTime(System.currentTimeMillis())
                .setStatus(DelayJobStatus.VALID.getStatus())
                .setTenantId(injectTenantId());
    }

    private TriggerWrapper buildTriggerParam(DelayJobParam param) {
        TriggerWrapper triggerWrapper = new TriggerWrapper();
        triggerWrapper.setJobName(param.getId());
        Map<String, Object> map = new HashMap<>();
        map.put(CommonConstants.jobName, param.getJobName());
        triggerWrapper.setJobDataMap(map);

        DailyTimeIntervalTriggerWrapper dailyParam = new DailyTimeIntervalTriggerWrapper();
        dailyParam.setTriggerName(idGenerator.getNextId());
        dailyParam.setStartTime(param.getStartTime());
        dailyParam.setRepeatInterval(param.getRepeatInterval());
        dailyParam.setTimeUnit(DateBuilder.IntervalUnit.SECOND.name());
        dailyParam.setRepeatCount(param.getRepeatCount());
        dailyParam.setEndTime(param.getEndTime());
        dailyParam.setMisfireInstruction(param.getMisfireInstruction());
        dailyParam.setCalendarNames(param.getCalendarNames());
        List<DailyTimeIntervalTriggerWrapper> list = new ArrayList<>();
        list.add(dailyParam);
        triggerWrapper.setDailyTimeIntervalTriggers(list);

        return triggerWrapper;
    }
}
