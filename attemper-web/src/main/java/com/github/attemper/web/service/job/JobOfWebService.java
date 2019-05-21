package com.github.attemper.web.service.job;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.github.attemper.common.result.dispatch.job.FlowJob;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.common.result.dispatch.trigger.sub.CommonTriggerResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.scheduler.service.JobCallingService;
import com.github.attemper.core.dao.mapper.job.JobMapper;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.job.TriggerService;
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
        Page<FlowJob> list = (Page<FlowJob>) mapper.list(paramMap);
        list.forEach(job -> setNextFireTime(job));
        return PageUtil.toResultMap(list);
    }

    private void setNextFireTime(FlowJob job) {
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
                    nextDateList.addAll(TriggerUtils.computeFireTimes((OperableTrigger) trigger, null, 1));// TODO calendar
                }
            } catch (SchedulerException e) {
                log.error(e.getMessage(), e);
                throw new RTException(6150, e);
            }
        }
        return nextDateList;
    }

    public FlowJob add(JobSaveParam param) {
        FlowJob flowJob = jobService.get(JobGetParam.builder().jobName(param.getJobName()).build());
        if (flowJob != null) {
            throw new DuplicateKeyException(param.getJobName());
        }
        flowJob = toBaseJob(param);
        Date now = new Date();
        flowJob.setCreateTime(now);
        flowJob.setUpdateTime(now);
        flowJob.setMaxReversion(1);
        flowJob.setReversion(1);
        if (param.getJobContent() == null) {
            BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(flowJob.getJobName())
                    .name(flowJob.getDisplayName())
                    .startEvent()
                    .id("StartEvent_1")
                    .done();
            flowJob.setJobContent(Bpmn.convertToString(modelInstance));
        } else {
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(param.getJobContent().getBytes()));
            Collection<Process> modelElements = bpmnModelInstance.getModelElementsByType(Process.class);
            for (Process process : modelElements) {
                process.builder().id(param.getJobName()).name(param.getDisplayName());
                break;
            }
            flowJob.setJobContent(Bpmn.convertToString(bpmnModelInstance));
        }
        mapper.add(flowJob);
        mapper.addInfo(flowJob);
        return flowJob;
    }

    public FlowJob update(JobSaveParam param) {
        FlowJob flowJob = jobService.get(JobGetParam.builder().jobName(param.getJobName()).build());
        if (flowJob == null) {
            return add(param); // Equivalent to copy then add
        }
        if (!StringUtils.equals(param.getDisplayName(), flowJob.getDisplayName())) {
            throw new RTException(6080);
        }
        FlowJob updatedJob = toBaseJob(param);
        updatedJob.setCreateTime(flowJob.getCreateTime());
        updatedJob.setMaxVersion(flowJob.getMaxVersion());
        if (checkNeedSave(flowJob, updatedJob)) {  // base info
            updatedJob.setMaxReversion(flowJob.getMaxReversion());
        } else {  //job content
            if (StringUtils.equals(param.getJobContent(), flowJob.getJobContent())) {
                throw new RTException(6056);
            }
            updatedJob.setUpdateTime(new Date());
            updatedJob.setDeploymentTime(null);
            if (flowJob.getVersion() == null) {
                updatedJob.setMaxReversion(flowJob.getReversion());
                updatedJob.setReversion(flowJob.getReversion());
                mapper.updateInfo(updatedJob);
            } else {
                updatedJob.setMaxReversion(flowJob.getReversion() + 1);
                updatedJob.setReversion(flowJob.getReversion() + 1);
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
            TriggerUpdateParam triggerUpdateParam = new TriggerUpdateParam(item);
            triggerOfWebService.update(triggerUpdateParam);
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
            FlowJob flowJob = validateAndGet(jobName);  //the newest reversion job
            if (flowJob.getVersion() != null) {
                throw new RTException(6054, jobName);
            }
            Deployment deployment = camundaHandler.createDefault(flowJob);
            int maxVersion = flowJob.getMaxVersion() == null ? 1 : flowJob.getMaxVersion() + 1;
            flowJob.setUpdateTime(deployment.getDeploymentTime());
            flowJob.setDeploymentTime(deployment.getDeploymentTime());
            flowJob.setVersion(maxVersion);
            flowJob.setMaxReversion(flowJob.getMaxReversion());
            flowJob.setMaxVersion(maxVersion);
            mapper.update(flowJob);
            mapper.updateInfo(flowJob);
        });
        return null;
    }

    /**
     * 判断是否需要更新模型版本
     *
     * @param flowJob
     * @param updatedJob
     * @return
     */
    private boolean checkNeedSave(FlowJob flowJob, FlowJob updatedJob) {
        return flowJob.getStatus() != updatedJob.getStatus()
                || flowJob.getTimeout() != updatedJob.getTimeout()
                || !StringUtils.equals(flowJob.getRemark(), updatedJob.getRemark());
    }

    private FlowJob toBaseJob(JobSaveParam saveParam) {
        return FlowJob.builder()
                .jobName(saveParam.getJobName())
                .displayName(saveParam.getDisplayName())
                .jobContent(saveParam.getJobContent())
                .status(saveParam.getStatus())
                .timeout(saveParam.getTimeout())
                .remark(saveParam.getRemark())
                .tenantId(injectTenantId())
                .build();
    }

    private FlowJob validateAndGet(String jobName) {
        JobGetParam jobGetParam = JobGetParam.builder().jobName(jobName).build();
        FlowJob flowJob = jobService.get(jobGetParam);
        if (flowJob == null) {
            throw new RTException(6050, jobName);
        }
        if (StringUtils.isBlank(flowJob.getJobContent())) {
            throw new RTException(6053, jobName);
        }
        return flowJob;
    }

    /**
     * copy job with current reversion to another job(if the target was existent, will add its reversion)
     *
     * @param param
     * @return
     */
    public FlowJob copy(JobCopyParam param) {
        JobSaveParam targetJobParam = param.getTargetJobParam();
        FlowJob sourceJob = jobService.get(JobGetParam.builder().jobName(param.getJobName()).reversion(param.getReversion()).build());
        FlowJob targetJob = jobService.get(JobGetParam.builder().jobName(targetJobParam.getJobName()).build());
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
    public FlowJob exchange(JobGetParam param) {
        FlowJob oldReversionJob = jobService.get(param);
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
                    throw e;
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
