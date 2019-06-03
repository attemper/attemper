package com.github.attemper.web.service.job;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerSaveParam;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.dao.mapper.job.JobMapper;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.job.TriggerService;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.attemper.web.service.CamundaHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.quartz.*;
import org.quartz.spi.OperableTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ldang
 */
@Slf4j
@Service
@Transactional
public class JobOfWebService extends BaseServiceAdapter {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobMapper mapper;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private TriggerOfWebService triggerOfWebService;

    @Autowired
    private CamundaHandler camundaHandler;

    @Autowired
    private Scheduler scheduler;

    public Map<String, Object> list(JobListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Job> list = (Page<Job>) mapper.list(paramMap);
        list.forEach(job -> setNextFireTime(job));
        return PageUtil.toResultMap(list);
    }

    private void setNextFireTime(Job job) {
        List<Date> allTriggerDates = new ArrayList<>();
        TriggerResult triggerResult = triggerService.get(new TriggerGetParam(job.getJobName()));
        allTriggerDates.addAll(getNextDateList(triggerResult.getCronTriggers()));
        allTriggerDates.addAll(getNextDateList(triggerResult.getCalendarOffsetTriggers()));
        allTriggerDates.addAll(getNextDateList(triggerResult.getDailyIntervalTriggers()));
        allTriggerDates.addAll(getNextDateList(triggerResult.getCalendarIntervalTriggers()));
        if (allTriggerDates.size() > 0) {
            Collections.sort(allTriggerDates);
            job.setNextFireTime(allTriggerDates.get(0));
        }
    }

    private List<Date> getNextDateList(List<? extends CommonTriggerResult> list) {
        List<Date> nextDateList = new ArrayList<>();
        if (list != null) {
            try {
                for (CommonTriggerResult item : list) {
                    Trigger trigger = scheduler.getTrigger(new TriggerKey(item.getTriggerName(), injectTenantId()));
                    if (trigger != null) {
                        nextDateList.addAll(TriggerUtils.computeFireTimes((OperableTrigger) trigger, null, 1));// TODO calendar
                    }
                }
            } catch (SchedulerException e) {
                log.error(e.getMessage(), e);
                throw new RTException(6150, e);
            }
        }
        return nextDateList;
    }

    public Job add(JobSaveParam param) {
        Job job = jobService.get(JobGetParam.builder().jobName(param.getJobName()).build());
        if (job != null) {
            throw new DuplicateKeyException(param.getJobName());
        }
        job = toBaseJob(param);
        Date now = new Date();
        job.setCreateTime(now);
        job.setUpdateTime(now);
        job.setMaxReversion(1);
        job.setReversion(1);
        if (param.getJobContent() == null) {
            BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(job.getJobName())
                    .name(job.getDisplayName())
                    .startEvent()
                    .id("StartEvent_1")
                    .done();
            job.setJobContent(Bpmn.convertToString(modelInstance));
        } else {
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(param.getJobContent().getBytes()));
            Collection<Process> modelElements = bpmnModelInstance.getModelElementsByType(Process.class);
            for (Process process : modelElements) {
                process.builder().id(param.getJobName()).name(param.getDisplayName());
                break;
            }
            job.setJobContent(Bpmn.convertToString(bpmnModelInstance));
        }
        mapper.add(job);
        mapper.addInfo(job);
        return job;
    }

    public Job update(JobSaveParam param) {
        Job job = jobService.get(JobGetParam.builder().jobName(param.getJobName()).build());
        if (job == null) {
            return add(param); // Equivalent to copy then add
        }
        if (!StringUtils.equals(param.getDisplayName(), job.getDisplayName())) {
            throw new RTException(6080);
        }
        Job updatedJob = toBaseJob(param);
        updatedJob.setCreateTime(job.getCreateTime());
        updatedJob.setMaxVersion(job.getMaxVersion());
        if (checkNeedSave(job, updatedJob)) {  // base info
            updatedJob.setMaxReversion(job.getMaxReversion());
        } else {  //job content
            if (StringUtils.equals(param.getJobContent(), job.getJobContent())) {
                throw new RTException(6056);
            }
            updatedJob.setUpdateTime(new Date());
            updatedJob.setDeploymentTime(null);
            if (job.getVersion() == null) {
                updatedJob.setMaxReversion(job.getReversion());
                updatedJob.setReversion(job.getReversion());
                mapper.updateInfo(updatedJob);
            } else {
                updatedJob.setMaxReversion(job.getReversion() + 1);
                updatedJob.setReversion(job.getReversion() + 1);
                updatedJob.setVersion(null);
                mapper.addInfo(updatedJob);
            }
        }
        mapper.update(updatedJob);
        return updatedJob;
    }

    /**
     * 删除
     *
     * @param param
     * @return
     */
    public Void remove(JobNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        param.getJobNames().forEach(item -> {
            TriggerSaveParam triggerSaveParam = new TriggerSaveParam(item);
            triggerOfWebService.update(triggerSaveParam);
        });
        mapper.delete(paramMap);
        camundaHandler.removeDefinitionAndDeployment(param.getJobNames(), injectTenantId());
        return null;
    }


    /**
     * publish a job to camunda engine
     *
     * @param param
     * @return
     */
    public Void publish(JobPublishParam param) {
        List<String> jobNames = param.getJobNames();
        jobNames.forEach(jobName -> {
            Job job = validateAndGet(jobName);  //the newest reversion job
            if (job.getVersion() != null) {
                throw new RTException(6054, jobName);
            }
            Deployment deployment = camundaHandler.createDefault(job);
            int maxVersion = job.getMaxVersion() == null ? 1 : job.getMaxVersion() + 1;
            job.setUpdateTime(deployment.getDeploymentTime());
            job.setDeploymentTime(deployment.getDeploymentTime());
            job.setVersion(maxVersion);
            job.setMaxReversion(job.getMaxReversion());
            job.setMaxVersion(maxVersion);
            mapper.update(job);
            mapper.updateInfo(job);
        });
        return null;
    }

    /**
     * 判断是否需要更新模型版本
     *
     * @param job
     * @param updatedJob
     * @return
     */
    private boolean checkNeedSave(Job job, Job updatedJob) {
        return job.getStatus() != updatedJob.getStatus()
                || job.getTimeout() != updatedJob.getTimeout()
                || !StringUtils.equals(job.getRemark(), updatedJob.getRemark());
    }

    private Job toBaseJob(JobSaveParam saveParam) {
        return Job.builder()
                .jobName(saveParam.getJobName())
                .displayName(saveParam.getDisplayName())
                .jobContent(saveParam.getJobContent())
                .status(saveParam.getStatus())
                .timeout(saveParam.getTimeout())
                .remark(saveParam.getRemark())
                .tenantId(injectTenantId())
                .build();
    }

    private Job validateAndGet(String jobName) {
        JobGetParam jobGetParam = JobGetParam.builder().jobName(jobName).build();
        Job job = jobService.get(jobGetParam);
        if (job == null) {
            throw new RTException(6050, jobName);
        }
        if (StringUtils.isBlank(job.getJobContent())) {
            throw new RTException(6053, jobName);
        }
        return job;
    }

    /**
     * copy job with current reversion to another job(if the target was existent, will add its reversion)
     *
     * @param param
     * @return
     */
    public Job copy(JobCopyParam param) {
        JobSaveParam targetJobParam = param.getTargetJobParam();
        Job sourceJob = jobService.get(JobGetParam.builder().jobName(param.getJobName()).reversion(param.getReversion()).build());
        Job targetJob = jobService.get(JobGetParam.builder().jobName(targetJobParam.getJobName()).build());
        if (targetJob != null) { // add its reversion with new model
            JobSaveParam saveParam = JobSaveParam.builder()
                    .jobName(targetJob.getJobName()).displayName(targetJob.getDisplayName())
                    .status(targetJob.getStatus()).timeout(targetJob.getTimeout())
                    .remark(targetJob.getRemark()).jobContent(sourceJob.getJobContent())
                    .build();
            return update(saveParam);
        }
        //add new model with reversion of 1
        targetJobParam.setJobContent(sourceJob.getJobContent());
        return add(targetJobParam);
    }

    /**
     * exchange current reversion to the latest
     *
     * @param param
     * @return
     */
    public Job exchange(JobGetParam param) {
        Job oldReversionJob = jobService.get(param);
        JobSaveParam saveParam = JobSaveParam.builder()
                .jobName(oldReversionJob.getJobName()).displayName(oldReversionJob.getDisplayName())
                .status(oldReversionJob.getStatus()).timeout(oldReversionJob.getTimeout())
                .remark(oldReversionJob.getRemark()).jobContent(oldReversionJob.getJobContent()).build();
        update(saveParam);
        return jobService.get(JobGetParam.builder().jobName(param.getJobName()).build());
    }

    /**
     * manual run jobs
     *
     * @param param
     * @return
     */
    public Void manual(JobNamesParam param) {
        List<String> jobNames = param.getJobNames();
        ExecutorService executorService = Executors.newFixedThreadPool(jobNames.size());
        String tenantId = injectTenantId();
        for (String jobName : jobNames) {
            executorService.submit(() -> {
                try {
                    return SpringContextAware.getBean(JobCallingService.class).invoke(jobName, tenantId);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            });
        }
        executorService.shutdown();
        return null;
    }

    public Void addArg(JobArgAllocatedParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.addArg(paramMap);
        return null;
    }

    public Void removeArg(JobArgAllocatedParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteArg(paramMap);
        return null;
    }
}
