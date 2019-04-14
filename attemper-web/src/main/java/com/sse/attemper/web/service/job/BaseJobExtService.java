package com.sse.attemper.web.service.job;

import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.dispatch.job.*;
import com.sse.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.common.result.dispatch.job.BaseJob;
import com.sse.attemper.core.dao.mapper.job.BaseJobMapper;
import com.sse.attemper.core.service.job.BaseJobService;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class BaseJobExtService extends BaseServiceAdapter {

    @Autowired
    private BaseJobService baseJobService;

    @Autowired
    private BaseJobMapper mapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TriggerExtService triggerService;

    /**
     * 新增
     * @param saveParam
     * @return
     */
    public BaseJob add(BaseJobSaveParam saveParam) {
        BaseJob baseJob = baseJobService.get(BaseJobGetParam.builder().jobName(saveParam.getJobName()).build());
        if (baseJob != null) {
            throw new DuplicateKeyException(saveParam.getJobName());
        }
        baseJob = toBaseJob(saveParam);
        Date now = new Date();
        baseJob.setCreateTime(now);
        baseJob.setUpdateTime(now);
        baseJob.setMaxReversion(1);
        baseJob.setReversion(1);
        if (saveParam.getJobContent() == null) {
            BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(baseJob.getJobName())
                    .name(baseJob.getDisplayName())
                    .startEvent()
                    .id("StartEvent_1")
                    .done();
            baseJob.setJobContent(Bpmn.convertToString(modelInstance));
        } else {
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(saveParam.getJobContent().getBytes()));
            Collection<Process> modelElements = bpmnModelInstance.getModelElementsByType(Process.class);
            for (Process process : modelElements) {
                process.builder().id(saveParam.getJobName()).name(saveParam.getDisplayName());
                break;
            }
            baseJob.setJobContent(Bpmn.convertToString(bpmnModelInstance));
        }
        mapper.add(baseJob);
        mapper.addInfo(baseJob);
        return baseJob;
    }

    /**
     * update model
     * @param saveParam
     * @return
     */
    public BaseJob update(BaseJobSaveParam saveParam) {
        BaseJob baseJob = baseJobService.get(BaseJobGetParam.builder().jobName(saveParam.getJobName()).build());
        if (baseJob == null) {
            return add(saveParam); // Equivalent to copy then add
        }
        if (!StringUtils.equals(saveParam.getDisplayName(), baseJob.getDisplayName())) {
            throw new RTException(6080);
        }
        BaseJob updatedJob = toBaseJob(saveParam);
        updatedJob.setCreateTime(baseJob.getCreateTime());
        updatedJob.setMaxVersion(baseJob.getMaxVersion());
        if (checkNeedSave(baseJob, updatedJob)) {  // status remark
            updatedJob.setMaxReversion(baseJob.getMaxReversion());
        } else {  //job content
            if (StringUtils.equals(saveParam.getJobContent(), baseJob.getJobContent())) {
                throw new RTException(6056);
            }
            updatedJob.setMaxReversion(baseJob.getReversion() + 1);
            updatedJob.setUpdateTime(new Date());
            updatedJob.setReversion(baseJob.getReversion() + 1);
            updatedJob.setVersion(null);
            updatedJob.setDeploymentTime(null);
            mapper.addInfo(updatedJob);
        }
        mapper.update(updatedJob);
        return updatedJob;
    }

    /**
     * 删除
     *
     * @param removeParam
     * @return
     */
    public Void remove(BaseJobRemoveParam removeParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(removeParam);
        removeParam.getJobNames().forEach(item -> {
            TriggerUpdateParam triggerUpdateParam = new TriggerUpdateParam(item);
            triggerService.update(triggerUpdateParam);
            /*List<String> oldTriggerNames = triggerService.getOldTriggerNames(triggerUpdateParam);
            TriggerChangedParam triggerChangedParam = new TriggerChangedParam(item, oldTriggerNames);
            schedulerHandler.updateTrigger(triggerChangedParam);*/
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
    public Void publish(BaseJobPublishParam param) {
        List<String> jobNames = param.getJobNames();
        jobNames.forEach(jobName -> {
            BaseJob baseJob = validateAndGet(jobName);  //the newest reversion job
            Deployment deployment = repositoryService.createDeployment()
                    .addModelInstance(jobName + ".bpmn20.xml", Bpmn.readModelFromStream(new ByteArrayInputStream(baseJob.getJobContent().getBytes())))
                    .name(baseJob.getDisplayName())
                    .tenantId(injectAdminedTenant().getId())
                    .deploy();
            int maxVersion = baseJob.getMaxVersion() == null ? 1 : baseJob.getMaxVersion() + 1;
            baseJob.setUpdateTime(deployment.getDeploymentTime());
            baseJob.setDeploymentTime(deployment.getDeploymentTime());
            baseJob.setVersion(maxVersion);
            baseJob.setMaxReversion(baseJob.getMaxReversion());
            baseJob.setMaxVersion(maxVersion);
            mapper.update(baseJob);
            mapper.updateInfo(baseJob);
        });
        return null;
    }

    /**
     * 判断是否需要更新模型版本
     *
     * @param baseJob
     * @param updatedJob
     * @return
     */
    private boolean checkNeedSave(BaseJob baseJob, BaseJob updatedJob) {
        return baseJob.getStatus() != updatedJob.getStatus() || !StringUtils.equals(baseJob.getRemark(), updatedJob.getRemark());
    }

    private BaseJob toBaseJob(BaseJobSaveParam saveParam) {
        return BaseJob.builder()
                .jobName(saveParam.getJobName())
                .displayName(saveParam.getDisplayName())
                .jobContent(saveParam.getJobContent())
                .status(saveParam.getStatus())
                .remark(saveParam.getRemark())
                .tenantId(injectAdminedTenant().getId())
                .build();
    }

    private BaseJob validateAndGet(String jobName) {
        BaseJobGetParam baseJobGetParam = BaseJobGetParam.builder().jobName(jobName).build();
        BaseJob baseJob = baseJobService.get(baseJobGetParam);
        if (baseJob == null) {
            throw new RTException(6050, jobName);
        }
        if (StringUtils.isBlank(baseJob.getJobContent())) {
            throw new RTException(6053, jobName);
        }
        return baseJob;
    }

    /**
     * copy job with current reversion to another job(if the target was existent, will add its reversion)
     *
     * @param param
     * @return
     */
    public BaseJob copy(BaseJobCopyParam param) {
        BaseJobSaveParam targetJobParam = param.getTargetJobParam();
        BaseJob sourceJob = baseJobService.get(BaseJobGetParam.builder().jobName(param.getJobName()).reversion(param.getReversion()).build());
        BaseJob targetJob = baseJobService.get(BaseJobGetParam.builder().jobName(targetJobParam.getJobName()).build());
        if (targetJob != null) { // add its reversion with new model
            BaseJobSaveParam saveParam = BaseJobSaveParam.builder().jobName(targetJob.getJobName())
                    .displayName(targetJob.getDisplayName()).status(targetJob.getStatus()).remark(targetJob.getRemark())
                    .jobContent(sourceJob.getJobContent()).build();
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
    public BaseJob exchange(BaseJobGetParam param) {
        BaseJob oldReversionJob = baseJobService.get(param);
        BaseJobSaveParam saveParam = BaseJobSaveParam.builder().jobName(oldReversionJob.getJobName())
                .displayName(oldReversionJob.getDisplayName()).status(oldReversionJob.getStatus())
                .remark(oldReversionJob.getRemark()).jobContent(oldReversionJob.getJobContent()).build();
        return update(saveParam);
    }
}
