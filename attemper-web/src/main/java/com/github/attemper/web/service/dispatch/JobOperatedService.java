package com.github.attemper.web.service.dispatch;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.JobStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.arg.ArgNamesParam;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.param.dispatch.trigger.TriggerWrapper;
import com.github.attemper.common.param.dispatch.trigger.sub.*;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.job.JobExportAndImportResult;
import com.github.attemper.common.result.dispatch.job.JobWithVersionResult;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.dao.dispatch.JobMapper;
import com.github.attemper.core.service.dispatch.ArgService;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.core.util.FileUtil;
import com.github.attemper.invoker.service.JobCallingService;
import com.github.attemper.invoker.util.QuartzUtil;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.attemper.web.ext.trigger.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.commons.utils.IoUtil;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.triggers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
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
    private IdGenerator idGenerator;

    @Autowired
    private JobCallingService jobCallingService;

    @Autowired
    private RepositoryService repositoryService;

    public Map<String, Object> list(JobListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Job> list = (Page<Job>) mapper.list(paramMap);
        list.forEach(this::setNextFireTime);
        return PageUtil.toResultMap(list);
    }

    private void setNextFireTime(Job job) {
        List<Date> allTriggerDates = new ArrayList<>(32);
        TriggerWrapper triggerResult = getTrigger(new JobNameParam().setJobName(job.getJobName()));
        if (triggerResult != null) {
            allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getCronTriggers(), injectTenantId()));
            allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getCalendarOffsetTriggers(), injectTenantId()));
            allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getDailyTimeIntervalTriggers(), injectTenantId()));
            allTriggerDates.addAll(QuartzUtil.getNextFireTimes(triggerResult.getCalendarIntervalTriggers(), injectTenantId()));
            if (allTriggerDates.size() > 0) {
                Collections.sort(allTriggerDates);
                job.setNextFireTimes(allTriggerDates.size() <= 10 ? allTriggerDates : allTriggerDates.subList(0, 10));
            }
        }
    }

    public Job add(JobSaveParam param) {
        Job job = jobService.get(new JobNameParam().setJobName(param.getJobName()));
        if (job != null) {
            throw new DuplicateKeyException(param.getJobName());
        }
        job = toJob(param);
        job.setUpdateTime(System.currentTimeMillis());
        if (StringUtils.isBlank(param.getContent())) {
            BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(job.getJobName())
                    .name(job.getDisplayName())
                    .startEvent()
                    .id("start")
                    .done();
            job.setContent(Bpmn.convertToString(modelInstance));
        } else {
            BpmnModelInstance bpmnModelInstance = toBpmnModelInstance(param.getContent());
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
        updatedJob.setUpdateTime(System.currentTimeMillis());
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

    public Void updateContent(JobContentSaveParam param) {
        Job job = validateAndGet(param.getJobName());
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
        job.setUpdateTime(System.currentTimeMillis());
        job.setContent(param.getContent());
        mapper.updateContent(job);
        return null;
    }

    public Void remove(JobNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        for (String jobName : param.getJobNames()) {
            TriggerWrapper triggerWrapper = new TriggerWrapper(jobName);
            updateTrigger(triggerWrapper);
        }
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
        for (String jobName : jobNames) {
            Job job = validateAndGet(jobName);
            if (StringUtils.isBlank(job.getContent())) {
                throw new RTException(6053, jobName);
            }
            Deployment deployment = deploy(job);
            job.setUpdateTime(deployment.getDeploymentTime().getTime()).setContent(null);
            mapper.updateContent(job);
        }
        return null;
    }

    private Job toJob(JobSaveParam saveParam) {
        return new Job()
                .setJobName(saveParam.getJobName())
                .setDisplayName(saveParam.getDisplayName())
                .setContent(saveParam.getContent())
                .setStatus(saveParam.getStatus())
                .setConcurrent(saveParam.getConcurrent())
                .setOnce(saveParam.getOnce())
                .setRemark(saveParam.getRemark())
                .setTenantId(injectTenantId());
    }

    public Job validateAndGet(String jobName) {
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
            JobSaveParam saveParam = toParam(targetJob);
            saveParam.setContent(content);
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
    public Void exchange(JobNameWithDefinitionParam param) {
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
                .setContent(IoUtil.inputStreamAsString(is));
        saveParam.setJobName(oldReversionJob.getJobName());
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
                    .setUpdateTime(new Date(job.getUpdateTime()))
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

    private static final String JSON_FILE_JOB = "job.json";

    private static final String JSON_FILE_ARG = "arg.json";

    private static final String JSON_FILE_CONDITION = "condition.json";

    /**
     * export job and(or) trigger and(or) arg and(or) project
     * @param response
     * @param param
     */
    public void exportModel(HttpServletResponse response, JobExportParam param) {
        List<JobExportAndImportResult> jobModels = new ArrayList<>();
        Set<Arg> argModels = new HashSet<>();
        Set<Condition> conditionModels = new HashSet<>();
        String tenantId = injectTenantId();
        for (String jobName : param.getJobNames()) {
            Job job = jobService.get(jobName, tenantId);
            job.setUpdateTime(null) // import will init it
                .setContent(null); // reject temporary design
            JobExportAndImportResult mainModel = new JobExportAndImportResult();
            mainModel.setJob(job);
            List<Arg> args = jobService.getArg(jobName, tenantId);
            if (args.size() > 0) {
                mainModel.setArgs(args.stream().map(Arg::getArgName).collect(Collectors.toList()));
                argModels.addAll(args);
            }
            List<Condition> conditions = jobService.getConditions(jobName, tenantId);
            if (conditions.size() > 0) {
                mainModel.setConditions(conditions.stream().map(Condition::getConditionName).collect(Collectors.toList()));
                conditionModels.addAll(conditions);
            }
            TriggerWrapper triggerResult = getTrigger(new JobNameParam().setJobName(jobName));
            if (triggerResult != null) {
                mainModel.setTrigger(triggerResult);
            }
            jobModels.add(mainModel);
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + param.getFileName());
        response.setContentType("application/zip");
        try (ServletOutputStream outputStream = response.getOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            if (jobModels.size() > 0) {
                String jobModelStr = BeanUtil.bean2JsonStr(jobModels);
                createZipEntry(zipOutputStream, JSON_FILE_JOB, IoUtil.stringAsInputStream(jobModelStr));
            }
            if (argModels.size() > 0) {
                String argModelStr = BeanUtil.bean2JsonStr(argModels);
                createZipEntry(zipOutputStream, JSON_FILE_ARG, IoUtil.stringAsInputStream(argModelStr));
            }
            if (conditionModels.size() > 0) {
                String conditionModelStr = BeanUtil.bean2JsonStr(conditionModels);
                createZipEntry(zipOutputStream, JSON_FILE_CONDITION, IoUtil.stringAsInputStream(conditionModelStr));
            }
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKeysIn(param.getJobNames().toArray(new String[]{}))
                    .tenantIdIn(injectTenantId())
                    .latestVersion() // export latest version
                    .list();
            for (ProcessDefinition definition : processDefinitions) {
                InputStream resourceAsStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
                createZipEntry(zipOutputStream, definition.getResourceName(), resourceAsStream);
            }
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
    }

    @Autowired
    private ArgService argService;

    public Void importModel(MultipartFile file) {
        String tenantId = injectTenantId();
        Map<String, String> nameDataMap = new HashMap<>();
        try (InputStream is = file.getInputStream();
             ZipInputStream zis = new ZipInputStream(is);){
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                nameDataMap.put(zipEntry.getName(), new String(FileUtil.inputStreamAsByteArray(zis), "UTF-8"));
            }
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
        if (nameDataMap.containsKey(JSON_FILE_ARG)) {
            String argModelStr = nameDataMap.get(JSON_FILE_ARG);
            if (StringUtils.isNotBlank(argModelStr)) {
                List<Map<String, Object>> argModels = BeanUtil.jsonStr2Bean(argModelStr, List.class);
                if (argModels.size() > 0) {
                    List<Arg> args = new ArrayList<>(argModels.size());
                    List<String> argNames = new ArrayList<>(argModels.size());
                    for (Map<String, Object> map : argModels) {
                        Arg arg = BeanUtil.map2Bean(Arg.class, map);
                        argNames.add(arg.getArgName());
                        arg.setTenantId(tenantId);   // replace tenantId
                        args.add(arg);
                    }
                    // 1.remove args
                    argService.remove(new ArgNamesParam().setArgNames(argNames));
                    // 2. insert args
                    argService.addBatch(args);
                }
            }
        }
        if (nameDataMap.containsKey(JSON_FILE_CONDITION)) {
            String conditionModelStr = nameDataMap.get(JSON_FILE_CONDITION);
            if (StringUtils.isNotBlank(conditionModelStr)) {
                List<Map<String, Object>> conditionModels = BeanUtil.jsonStr2Bean(conditionModelStr, List.class);
                if (conditionModels.size() > 0) {
                    List<Condition> conditions = new ArrayList<>(conditionModels.size());
                    List<Map<String, Object>> mapList = new ArrayList<>(conditionModels.size());
                    for (Map<String, Object> map : conditionModels) {
                        Condition condition = BeanUtil.map2Bean(Condition.class, map);
                        condition.setTenantId(tenantId);   // replace tenantId
                        conditions.add(condition);
                        mapList.add(BeanUtil.bean2Map(condition));
                    }
                    // 1.remove conditions
                    jobService.removeConditions(conditions);
                    // 2. add conditions
                    jobService.addConditions(mapList);
                }
            }
        }
        if (nameDataMap.containsKey(JSON_FILE_JOB)) {
            String jobModelStr = nameDataMap.get(JSON_FILE_JOB);
            if (StringUtils.isNotBlank(jobModelStr)) {
                List<Map<String, Object>> jobModels = BeanUtil.jsonStr2Bean(jobModelStr, List.class);
                if (jobModels.size() > 0) {
                    for (Map<String, Object> map : jobModels) {
                        JobExportAndImportResult exportedModel = BeanUtil.map2Bean(JobExportAndImportResult.class, map);
                        Job job = exportedModel.getJob();
                        if (job != null) {
                            job.setTenantId(tenantId);
                            Job jobInDb = jobService.get(job.getJobName(), tenantId);
                            if (jobInDb == null) { // insert
                                if (nameDataMap.containsKey(job.getJobName() + SUFFIX_BPMN_XML)) { // need publish
                                    String content = nameDataMap.get(job.getJobName() + SUFFIX_BPMN_XML);
                                    job.setContent(content);
                                    Deployment deployment = deploy(job);
                                    job.setUpdateTime(deployment.getDeploymentTime().getTime()).setContent(null);
                                } else {
                                    job.setUpdateTime(System.currentTimeMillis());
                                }
                                mapper.add(job);
                            } else { // update
                                if (nameDataMap.containsKey(job.getJobName() + SUFFIX_BPMN_XML)) { // need publish
                                    String content = nameDataMap.get(job.getJobName() + SUFFIX_BPMN_XML);
                                    job.setContent(content);
                                    Deployment deployment = deploy(job);
                                    job.setUpdateTime(deployment.getDeploymentTime().getTime()).setContent(null);
                                } else {
                                    job.setUpdateTime(System.currentTimeMillis()).setContent(jobInDb.getContent());
                                }
                                mapper.update(job);
                            }
                            List<String> argNames = exportedModel.getArgs();
                            if (argNames != null && argNames.size() > 0) {
                                saveJobArg(job.getJobName(), argNames);
                            }
                            List<String> conditionNames = exportedModel.getConditions();
                            if (conditionNames != null && conditionNames.size() > 0) {
                                saveJobCondition(job.getJobName(), conditionNames);
                            }
                            TriggerWrapper triggerResult = exportedModel.getTrigger();
                            if (triggerResult != null) {
                                triggerResult.setJobName(job.getJobName());
                                updateTrigger(triggerResult);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * you can't change the order of the array.<br>
     */
    private static TriggerWithQuartzHandler[] triggerHandlers = new TriggerWithQuartzHandler[]{
            new CronTriggerWithQuartzHandler(),
            new CalendarOffsetTriggerWithQuartzHandler(),
            new DailyTimeIntervalTriggerWithQuartzHandler(),
            new CalendarIntervalTriggerWithQuartzHandler()
    };

    @Autowired
    private Scheduler scheduler;

    public TriggerWrapper getTrigger(JobNameParam param) {
        List<CronTriggerWrapper> cronTriggerResults = new ArrayList<>();
        List<CalendarOffsetTriggerWrapper> calendarOffsetTriggerResults = new ArrayList<>();
        List<DailyTimeIntervalTriggerWrapper> dailyTimeIntervalResults = new ArrayList<>();
        List<CalendarIntervalTriggerWrapper> calendarIntervalTriggerResults = new ArrayList<>();
        try {
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(new JobKey(param.getJobName(), injectTenantId()));
            if (triggersOfJob.size() == 0) {
                return null;
            }
            for (Trigger trigger : triggersOfJob) {
                if (trigger instanceof CronTriggerImpl) {
                    cronTriggerResults.add((CronTriggerWrapper) triggerHandlers[0].getTrigger(trigger));
                } else if (trigger instanceof CalendarOffsetTriggerImpl) {
                    calendarOffsetTriggerResults.add((CalendarOffsetTriggerWrapper) triggerHandlers[1].getTrigger(trigger));
                } else if (trigger instanceof DailyTimeIntervalTriggerImpl || trigger instanceof SimpleTriggerImpl) {
                    dailyTimeIntervalResults.add((DailyTimeIntervalTriggerWrapper) triggerHandlers[2].getTrigger(trigger));
                } else if (trigger instanceof CalendarIntervalTriggerImpl) {
                    calendarIntervalTriggerResults.add((CalendarIntervalTriggerWrapper) triggerHandlers[3].getTrigger(trigger));
                } else {
                    throw new RTException(trigger.toString());
                }
            }
            TriggerWrapper result = new TriggerWrapper();
            if (cronTriggerResults.size() > 0) {
                result.setCronTriggers(cronTriggerResults);
            }
            if (calendarOffsetTriggerResults.size() > 0) {
                result.setCalendarOffsetTriggers(calendarOffsetTriggerResults);
            }
            if (dailyTimeIntervalResults.size() > 0) {
                result.setDailyTimeIntervalTriggers(dailyTimeIntervalResults);
            }
            if (calendarIntervalTriggerResults.size() > 0) {
                result.setCalendarIntervalTriggers(calendarIntervalTriggerResults);
            }
            return result;
        } catch (SchedulerException e) {
            throw new RTException(3002, e);
        }
    }

    public Void updateTrigger(TriggerWrapper param) {
        try {
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(new JobKey(param.getJobName(), injectTenantId()));
            if (triggersOfJob.size() > 0) {
                scheduler.unscheduleJobs(triggersOfJob.stream().map(Trigger::getKey).collect(Collectors.toList()));
            }
        } catch (SchedulerException e) {
            throw new RTException(3004, e);
        }
        Map<Integer, List<? extends CommonTriggerWrapper>> paramsOfTriggerMap = new HashMap<>(4);
        paramsOfTriggerMap.put(0, param.getCronTriggers());
        paramsOfTriggerMap.put(1, param.getCalendarOffsetTriggers());
        paramsOfTriggerMap.put(2, param.getDailyTimeIntervalTriggers());
        paramsOfTriggerMap.put(3, param.getCalendarIntervalTriggers());
        for (int i = 0; i < triggerHandlers.length; i++) {
            triggerHandlers[i].schedule(param.getJobName(), injectTenantId(), param.getJobDataMap(), paramsOfTriggerMap.get(i));
        }
        return null;
    }

    public Void validateAndUpdateTrigger(TriggerWrapper param) {
        validatePublished(param.getJobName());
        return updateTrigger(param);
    }

    public List<Date> testCron(CronTriggerWrapper param) {
        Set<Trigger> triggers = QuartzUtil.buildCronTriggers(injectTenantId(), Arrays.asList(param));
        return QuartzUtil.getNextFireTimes(triggers, param.getCalendarNames());
    }

    public List<Date> testCalendarOffset(CalendarOffsetTriggerWrapper param) {
        Set<Trigger> triggers = QuartzUtil.buildCalendarOffsetTriggers(injectTenantId(), Arrays.asList(param));
        return QuartzUtil.getNextFireTimes(triggers, param.getCalendarNames());
    }

    public List<Date> testDailyTimeInterval(DailyTimeIntervalTriggerWrapper param) {
        Set<Trigger> triggers = QuartzUtil.buildDailyIntervalTriggers(injectTenantId(), Arrays.asList(param));
        return QuartzUtil.getNextFireTimes(triggers, param.getCalendarNames());
    }

    public List<Date> testCalendarInterval(CalendarIntervalTriggerWrapper param) {
        Set<Trigger> triggers = QuartzUtil.buildCalendarIntervalTriggers(injectTenantId(), Arrays.asList(param));
        return QuartzUtil.getNextFireTimes(triggers, param.getCalendarNames());
    }

    /**
     * the job was published to camunda or not
     * @param jobName
     * @return
     */
    public void validatePublished(String jobName) {
        long count = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(jobName)
                .tenantIdIn(injectTenantId())
                .count();
        if (count <= 0){
            throw new RTException(6058, jobName);
        }
    }

    private JobSaveParam toParam(Job job) {
        JobSaveParam param = new JobSaveParam()
                .setDisplayName(job.getDisplayName()).setStatus(job.getStatus())
                .setConcurrent(job.getConcurrent()).setOnce(job.getOnce())
                .setRemark(job.getRemark());
        param.setJobName(job.getJobName());
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

    public void saveJobArg(String jobName, List<String> argNames) {
        JobArgSaveParam param = new JobArgSaveParam().setArgNames(argNames);
        param.setJobName(jobName);
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteJobArg(paramMap);
        if (param.getArgNames() != null && param.getArgNames().size() > 0) {
            mapper.addJobArg(paramMap);
        }
    }

    public void saveJobCondition(String jobName, List<String> conditionNames) {
        JobConditionSaveParam param = new JobConditionSaveParam().setConditionNames(conditionNames);
        param.setJobName(jobName);
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteJobCondition(paramMap);
        if (param.getConditionNames() != null && param.getConditionNames().size() > 0) {
            mapper.addJobCondition(paramMap);
        }
    }

    private Deployment deploy(Job job) {
        return repositoryService.createDeployment()
                .addModelInstance(job.getJobName() + SUFFIX_BPMN_XML,
                        toBpmnModelInstance(job.getContent()))
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

    private BpmnModelInstance toBpmnModelInstance(String xmlContent) {
        try {
            return Bpmn.readModelFromStream(new ByteArrayInputStream(xmlContent.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RTException(1100, e);
        }
    }

    private static final String SUFFIX_BPMN_XML = ".bpmn20.xml";
}
