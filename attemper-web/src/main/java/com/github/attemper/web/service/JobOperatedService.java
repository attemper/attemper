package com.github.attemper.web.service;

import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerSaveParam;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.dao.mapper.job.JobMapper;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.job.TriggerService;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.attemper.web.util.SupportUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@Transactional
public class JobOperatedService extends BaseServiceAdapter {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobMapper mapper;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private TriggerOperatedService triggerOperatedService;

    @Autowired
    private IdGenerator idGenerator;

    public Map<String, Object> list(JobListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Job> list = (Page<Job>) mapper.list(paramMap);
        list.forEach(job -> setNextFireTime(job));
        return PageUtil.toResultMap(list);
    }

    private void setNextFireTime(Job job) {
        List<Date> allTriggerDates = new ArrayList<>();
        TriggerResult triggerResult = triggerService.get(new TriggerGetParam().setJobName(job.getJobName()));
        allTriggerDates.addAll(SupportUtil.getNextDateList(triggerResult.getCronTriggers(), injectTenantId()));
        allTriggerDates.addAll(SupportUtil.getNextDateList(triggerResult.getCalendarOffsetTriggers(), injectTenantId()));
        allTriggerDates.addAll(SupportUtil.getNextDateList(triggerResult.getDailyIntervalTriggers(), injectTenantId()));
        allTriggerDates.addAll(SupportUtil.getNextDateList(triggerResult.getCalendarIntervalTriggers(), injectTenantId()));
        if (allTriggerDates.size() > 0) {
            Collections.sort(allTriggerDates);
            job.setNextFireTimes(allTriggerDates);
        }
    }

    public Job add(JobSaveParam param) {
        Job job = jobService.get(new JobGetParam().setJobName(param.getJobName()));
        if (job != null) {
            throw new DuplicateKeyException(param.getJobName());
        }
        job = toJob(param);
        if (job.getStatus() != JobStatus.ENABLED.getStatus()) {
            throw new RTException(6055);
        }
        Date now = new Date();
        job.setCreateTime(now);
        job.setUpdateTime(now);
        job.setMaxReversion(1);
        job.setReversion(1);
        if (StringUtils.isBlank(param.getJobContent())) {
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
        Job job = jobService.get(new JobGetParam().setJobName(param.getJobName()));
        if (job == null) {
            return add(param); // Equivalent to copy then add
        }
        if (!StringUtils.equals(param.getDisplayName(), job.getDisplayName())) {
            throw new RTException(6080);
        }
        Job updatedJob = toJob(param);
        updatedJob.setCreateTime(job.getCreateTime());
        updatedJob.setMaxVersion(job.getMaxVersion());
        if (checkNeedSave(job, updatedJob)) {  // base info
            updatedJob.setMaxReversion(job.getMaxReversion());
        } else {  //job content
            if (job.getStatus() != JobStatus.ENABLED.getStatus()) {
                throw new RTException(6057);
            }
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

    public Void remove(JobNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        param.getJobNames().forEach(item -> {
            TriggerSaveParam triggerSaveParam = new TriggerSaveParam(item);
            triggerOperatedService.update(triggerSaveParam);
        });
        mapper.delete(paramMap);
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
            Deployment deployment = createDefault(job);
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
     * if the job meta setting was changed, just save
     *
     * @param job
     * @param updatedJob
     * @return
     */
    private boolean checkNeedSave(Job job, Job updatedJob) {
        return job.getStatus() != updatedJob.getStatus()
                || job.getTimeout() != updatedJob.getTimeout()
                || job.isConcurrent() != updatedJob.isConcurrent()
                || !StringUtils.equals(job.getRemark(), updatedJob.getRemark());
    }

    private Job toJob(JobSaveParam saveParam) {
        return new Job()
                .setJobName(saveParam.getJobName())
                .setDisplayName(saveParam.getDisplayName())
                .setJobContent(saveParam.getJobContent())
                .setStatus(saveParam.getStatus())
                .setTimeout(saveParam.getTimeout())
                .setConcurrent(saveParam.getConcurrent())
                .setRemark(saveParam.getRemark())
                .setTenantId(injectTenantId());
    }

    private Job validateAndGet(String jobName) {
        JobGetParam jobGetParam = new JobGetParam().setJobName(jobName);
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
        Job sourceJob = jobService.get(new JobGetParam().setJobName(param.getJobName()).setReversion(param.getReversion()));
        Job targetJob = jobService.get(new JobGetParam().setJobName(targetJobParam.getJobName()));
        if (targetJob != null) { // add its reversion with new model
            JobSaveParam saveParam = new JobSaveParam()
                    .setJobName(targetJob.getJobName()).setDisplayName(targetJob.getDisplayName())
                    .setStatus(targetJob.getStatus()).setTimeout(targetJob.getTimeout())
                    .setConcurrent(targetJob.isConcurrent())
                    .setRemark(targetJob.getRemark()).setJobContent(sourceJob.getJobContent());
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
        JobSaveParam saveParam = new JobSaveParam()
                .setJobName(oldReversionJob.getJobName()).setDisplayName(oldReversionJob.getDisplayName())
                .setStatus(oldReversionJob.getStatus()).setTimeout(oldReversionJob.getTimeout())
                .setConcurrent(oldReversionJob.isConcurrent())
                .setRemark(oldReversionJob.getRemark()).setJobContent(oldReversionJob.getJobContent());
        update(saveParam);
        return jobService.get(new JobGetParam().setJobName(param.getJobName()));
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
                String id = idGenerator.getNextId();
                try {
                    SpringContextAware.getBean(JobCallingService.class).manual(id, jobName, tenantId, null);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
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

    @Autowired
    private RepositoryService repositoryService;

    private Deployment createDefault(Job job) {
        return repositoryService.createDeployment()
                .addModelInstance(job.getJobName() + ".bpmn20.xml",
                        Bpmn.readModelFromStream(new ByteArrayInputStream(job.getJobContent().getBytes())))
                .name(job.getDisplayName())
                .tenantId(job.getTenantId())
                .deploy();
    }
}
