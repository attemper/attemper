package com.github.attemper.web.service;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerSaveParam;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.job.JobWithVersionResult;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.mapper.job.JobMapper;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.core.service.job.TriggerService;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.net.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.commons.utils.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

    @Autowired
    private JobCallingService jobCallingService;

    @Autowired
    private RepositoryService repositoryService;

    public Map<String, Object> list(JobListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Job> list = (Page<Job>) mapper.list(paramMap);
        list.forEach(job -> setNextFireTime(job));
        return PageUtil.toResultMap(list);
    }

    private void setNextFireTime(Job job) {
        List<Date> allTriggerDates = new ArrayList<>(16);
        TriggerResult triggerResult = triggerService.get(new TriggerGetParam().setJobName(job.getJobName()));
        allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getCronTriggers(), injectTenantId()));
        allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getCalendarOffsetTriggers(), injectTenantId()));
        allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getDailyIntervalTriggers(), injectTenantId()));
        allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getCalendarIntervalTriggers(), injectTenantId()));
        if (allTriggerDates.size() > 0) {
            Collections.sort(allTriggerDates);
            job.setNextFireTimes(allTriggerDates);
        }
    }

    public Job add(JobSaveParam param) {
        Job job = jobService.get(new JobNameParam().setJobName(param.getJobName()));
        if (job != null) {
            throw new DuplicateKeyException(param.getJobName());
        }
        job = toJob(param);
        if (job.getStatus() != JobStatus.ENABLED.getStatus()) {
            throw new RTException(6055);
        }
        job.setUpdateTime(new Date());
        if (StringUtils.isBlank(param.getContent())) {
            BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(job.getJobName())
                    .name(job.getDisplayName())
                    .startEvent()
                    .id(job.getJobName() + "_start")
                    .done();
            job.setContent(Bpmn.convertToString(modelInstance));
        } else {
            BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(param.getContent().getBytes()));
            Collection<Process> modelElements = bpmnModelInstance.getModelElementsByType(Process.class);
            for (Process process : modelElements) {
                process.builder().id(param.getJobName()).name(param.getDisplayName());
                break;
            }
            job.setContent(Bpmn.convertToString(bpmnModelInstance));
        }
        mapper.add(job);
        return job;
    }

    public Job update(JobSaveParam param) {
        Job job = jobService.get(new JobNameParam().setJobName(param.getJobName()));
        if (job == null) {
            return add(param); // Equivalent to copy then add
        }
        if (!StringUtils.equals(param.getDisplayName(), job.getDisplayName())) {
            throw new RTException(6080);
        }
        Job updatedJob = toJob(param);
        updatedJob.setUpdateTime(new Date());
        mapper.update(updatedJob);
        return updatedJob;
    }

    public String getContent(JobNameWithDefinitionParam param) {
        if (StringUtils.isBlank(param.getProcDefId())) {
            Job job = validateAndGet(param.getJobName());
            return job.getContent();
        } else {
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(param.getProcDefId())
                    .singleResult();
            InputStream is = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
            return IoUtil.inputStreamAsString(is);
        }
    }

    public JobWithVersionResult updateContent(JobContentSaveParam param) {
        Job job = validateAndGet(param.getJobName());
        if (job.getStatus() != JobStatus.ENABLED.getStatus()) {
            throw new RTException(6057);
        }
        JobWithVersionResult result = new JobWithVersionResult()
                .setJobName(param.getJobName());
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(param.getJobName())
                .tenantIdIn(injectTenantId())
                .latestVersion()
                .singleResult();
        if (StringUtils.isNotBlank(job.getContent())) {
            if (StringUtils.equals(param.getContent(), job.getContent())) {
                throw new RTException(6056);
            }
        } else {
            if (definition != null) {
                InputStream is = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
                String latestContent = IoUtil.inputStreamAsString(is);
                if (StringUtils.equals(param.getContent(), latestContent)) {
                    throw new RTException(6056);
                }
            }
        }
        job.setUpdateTime(new Date());
        job.setContent(param.getContent());
        mapper.updateContent(job);
        if (definition == null) {
            result.setVersion(0);
        } else {
            result.setProcDefId(definition.getId()).setVersion(definition.getVersion() + 1);
        }
        return result.setUpdateTime(job.getUpdateTime());
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
    public Void publish(JobNamesParam param) {
        List<String> jobNames = param.getJobNames();
        jobNames.forEach(jobName -> {
            Job job = validateAndGet(jobName);
            if (StringUtils.isBlank(job.getContent())) {
                throw new RTException(6053, jobName);
            }
            Deployment deployment = deploy(job);
            job.setUpdateTime(deployment.getDeploymentTime()).setContent(null);
            mapper.updateContent(job);
        });
        return null;
    }

    private Job toJob(JobSaveParam saveParam) {
        return new Job()
                .setJobName(saveParam.getJobName())
                .setDisplayName(saveParam.getDisplayName())
                .setContent(saveParam.getContent())
                .setStatus(saveParam.getStatus())
                .setConcurrent(saveParam.isConcurrent())
                .setRemark(saveParam.getRemark())
                .setTenantId(injectTenantId());
    }

    private Job validateAndGet(String jobName) {
        JobNameParam jobNameParam = new JobNameParam().setJobName(jobName);
        Job job = jobService.get(jobNameParam);
        if (job == null) {
            throw new RTException(6050, jobName);
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
        Job sourceJob = validateAndGet(param.getJobName());
        Job targetJob = jobService.get(new JobNameParam().setJobName(targetJobParam.getJobName()));
        String content;
        if (StringUtils.isBlank(param.getProcDefId())) {
            content = sourceJob.getContent();
        } else {
            ProcessDefinition processDefinition = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionId(param.getProcDefId())
                    .singleResult();
            if (processDefinition == null) {
                throw new RTException(6059, param.getJobName());
            }
            InputStream is = repositoryService.getResourceAsStream(
                    processDefinition.getDeploymentId(), processDefinition.getResourceName());
            content = IoUtil.inputStreamAsString(is);
        }
        if (targetJob != null) {
            JobSaveParam saveParam = new JobSaveParam()
                    .setDisplayName(targetJob.getDisplayName())
                    .setStatus(targetJob.getStatus())
                    .setConcurrent(targetJob.isConcurrent()).setRemark(targetJob.getRemark());
            saveParam.setJobName(targetJob.getJobName()).setContent(content);
            return update(saveParam);
        }
        targetJobParam.setContent(content);
        return add(targetJobParam);
    }

    /**
     * exchange current reversion to the latest
     *
     * @param param
     * @return
     */
    public JobWithVersionResult exchange(JobNameWithDefinitionParam param) {
        Job oldReversionJob = validateAndGet(param.getJobName());
        if (StringUtils.isBlank(param.getProcDefId())) {
            return null;
        }
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(param.getProcDefId())
                .singleResult();
        if (processDefinition == null) {
            throw new RTException(6059, param.getJobName());
        }
        InputStream is = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), processDefinition.getResourceName());
        JobContentSaveParam saveParam = new JobContentSaveParam()
                .setJobName(oldReversionJob.getJobName())
                .setContent(IoUtil.inputStreamAsString(is));
        return updateContent(saveParam);
    }

    /**
     * list all versions by a specified job displayName
     *
     * @param param
     * @return
     */
    public List<JobWithVersionResult> versions(JobNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        List<JobWithVersionResult> jobWithVersionResults = mapper.versions(paramMap);
        Job job = validateAndGet(param.getJobName());
        if (StringUtils.isNotBlank(job.getContent())) {
            JobWithVersionResult result = new JobWithVersionResult()
                    .setJobName(job.getJobName())
                    .setUpdateTime(job.getUpdateTime())
                    .setVersion(jobWithVersionResults.isEmpty() ? 0 :
                            jobWithVersionResults.get(jobWithVersionResults.size() - 1).getVersion() + 1);
            jobWithVersionResults.add(result);
        }
        return jobWithVersionResults;
    }

    /**
     * manual run jobs
     *
     * @param param
     * @return
     */
    public Void manualBatch(JobNamesParam param) {
        List<String> jobNames = param.getJobNames();
        ExecutorService executorService = Executors.newFixedThreadPool(jobNames.size());
        String tenantId = injectTenantId();
        for (String jobName : jobNames) {
            executorService.submit(() -> {
                String id = idGenerator.getNextId();
                try {
                    jobCallingService.manual(id, jobName, tenantId, null);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        executorService.shutdown();
        return null;
    }

    /**
     * manual run job with param
     *
     * @param param
     * @return
     */
    public Void manual(JobNameWithJsonArgParam param) {
        String id = idGenerator.getNextId();
        String tenantId = injectTenantId();
        jobCallingService.manual(id, param.getJobName(), tenantId, BeanUtil.bean2Map(param.getJsonData()));
        return null;
    }

    public void exportModel(HttpServletResponse response, JobNamesParam param) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeysIn(param.getJobNames().toArray(new String[]{}))
                .tenantIdIn(injectTenantId())
                .latestVersion()
                .list();
        response.setHeader("Content-Disposition", "attachment; filename=models.zip");
        response.setContentType(MediaType.ZIP.toString());
        try (ServletOutputStream outputStream = response.getOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            for (ProcessDefinition definition : processDefinitions) {
                InputStream resourceAsStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
                createZipEntry(zipOutputStream, definition.getResourceName(), resourceAsStream);
            }
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
    }

    public Void importModel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             ZipInputStream zis = new ZipInputStream(is);) {
            Set<String> jobNames = new HashSet<>();
            while ((zis.getNextEntry()) != null) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(zis));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(sb.toString().getBytes()));
                Collection<Process> elements = bpmnModelInstance.getModelElementsByType(Process.class);
                for (Process process : elements) {
                    Job job = jobService.get(new JobNameParam().setJobName(process.getId()));
                    JobSaveParam jobSaveParam = new JobSaveParam()
                            .setDisplayName(process.getName()).setConcurrent(false)
                            .setStatus(JobStatus.ENABLED.getStatus());
                    jobSaveParam.setJobName(process.getId()).setContent(sb.toString());
                    if (job == null) {
                        add(jobSaveParam);
                    } else {
                        JobSaveParam saveParam = toParam(job);
                        saveParam.setContent(sb.toString());
                        update(saveParam);
                    }
                    jobNames.add(process.getId());
                }
            }
            publish(new JobNamesParam().setJobNames(new ArrayList<>(jobNames)));
        } catch (IOException e) {
            throw new RTException(1100, e);
        }

        return null;
    }

    private JobSaveParam toParam(Job job) {
        JobSaveParam param = new JobSaveParam()
                .setDisplayName(job.getDisplayName()).setStatus(job.getStatus())
                .setConcurrent(job.isConcurrent()).setRemark(job.getRemark());
        param.setJobName(job.getJobName()).setContent(job.getContent());
        return param;
    }

    private void createZipEntry(ZipOutputStream zipOutputStream, String fileName, InputStream is) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(IoUtil.inputStreamAsByteArray(is));
        zipOutputStream.closeEntry();
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

    private Deployment deploy(Job job) {
        return repositoryService.createDeployment()
                .addModelInstance(job.getJobName() + ".bpmn20.xml",
                        Bpmn.readModelFromStream(new ByteArrayInputStream(job.getContent().getBytes())))
                .name(job.getDisplayName())
                .tenantId(job.getTenantId())
                .deploy();
    }

    public Void enable(JobNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        paramMap.put(CommonConstants.status, JobStatus.ENABLED.getStatus());
        mapper.updateStatus(paramMap);
        return null;
    }

    public Void disable(JobNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        paramMap.put(CommonConstants.status, JobStatus.DISABLED.getStatus());
        mapper.updateStatus(paramMap);
        return null;
    }
}
