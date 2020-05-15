package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.condition.ConditionSaveParam;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.param.dispatch.trigger.TriggerWrapper;
import com.github.attemper.common.param.dispatch.trigger.sub.CalendarIntervalTriggerWrapper;
import com.github.attemper.common.param.dispatch.trigger.sub.CalendarOffsetTriggerWrapper;
import com.github.attemper.common.param.dispatch.trigger.sub.CronTriggerWrapper;
import com.github.attemper.common.param.dispatch.trigger.sub.DailyTimeIntervalTriggerWrapper;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.dispatch.condition.ConditionResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.job.JobWithVersionResult;
import com.github.attemper.core.service.dispatch.JobService;
import com.github.attemper.web.service.dispatch.JobOperatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Tag(name ="Job")
@RestController
public class JobController {
	
	@Autowired
	private JobService jobService;

	@Autowired
	private JobOperatedService jobOperatedService;

	@Operation(summary = "List jobs", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.$)
	public CommonResult<Map<String, Object>> list(JobListParam param) {
		return CommonResult.putResult(jobOperatedService.list(param));
	}

	@Operation(summary = "Add job", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.$)
	public CommonResult<Job> add(@RequestBody JobSaveParam param) {
		return CommonResult.putResult(jobOperatedService.add(param));
	}

	@Operation(summary = "Update job", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.$)
	public CommonResult<Job> update(@RequestBody JobSaveParam param) {
		return CommonResult.putResult(jobOperatedService.update(param));
	}

	@Operation(summary = "Update job content", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.CONTENT)
	public CommonResult<Void> updateContent(@RequestBody JobContentSaveParam param) {
		return CommonResult.putResult(jobOperatedService.updateContent(param));
	}

	@Operation(summary = "Remove jobs", parameters = {@Parameter(name = "param", required = true)})
	@DeleteMapping(APIPath.JobPath.$)
	public CommonResult<Void> remove(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.remove(param));
	}

	@Operation(summary = "Enable job", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.ENABLE)
	public CommonResult<Void> enable(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.enable(param));
	}

	@Operation(summary = "Disable job", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.DISABLE)
	public CommonResult<Void> disable(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.disable(param));
	}

	@Operation(summary = "Get job", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.GET)
	public CommonResult<Job> get(JobNameParam param) {
		return CommonResult.putResult(jobService.get(param));
	}

	@Operation(summary = "Get job content", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.CONTENT)
    public CommonResult<String> getContent(JobNameWithDefinitionParam param) {
		return CommonResult.putResult(jobOperatedService.getContent(param));
	}

	@Operation(summary = "List jobs by versions", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.VERSIONS)
	public CommonResult<List<JobWithVersionResult>> listVersions(JobNameParam param) {
		return CommonResult.putResult(jobOperatedService.versions(param));
	}

	@Operation(summary = "Publish job to quartz and camunda", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.PUBLISH)
	public CommonResult<Void> publish(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.publish(param));
	}

	@Operation(summary = "Copy job", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.COPY)
	public CommonResult<Job> copy(@RequestBody JobCopyParam param) {
		return CommonResult.putResult(jobOperatedService.copy(param));
	}

	@Operation(summary = "Exchange current reversion to the latest reversion", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.EXCHANGE)
    public CommonResult<Void> exchange(@RequestBody JobNameWithDefinitionParam param) {
		return CommonResult.putResult(jobOperatedService.exchange(param));
	}

	@Operation(summary = "Manual start jobs", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.MANUAL_BATCH)
	public CommonResult<Void> manualBatch(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.manualBatch(param));
	}

	@Operation(summary = "Manual start job with param", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.MANUAL)
	public CommonResult<Void> manual(@RequestBody JobNameWithJsonArgParam param) {
		return CommonResult.putResult(jobOperatedService.manual(param));
	}

	@Operation(summary = "Export model", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.EXPORT_MODEL)
	public void exportModel(HttpServletResponse response, JobExportParam param) {
		jobOperatedService.exportModel(response, param);
	}

	@Operation(summary = "Import model", parameters = {@Parameter(name = "file", required = true)})
	@PostMapping(APIPath.JobPath.IMPORT_MODEL)
	public CommonResult<Void> importModel(MultipartFile file) {
		return CommonResult.putResult(jobOperatedService.importModel(file));
	}

	@Operation(summary = "Get trigger info by job", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.GET_TRIGGER)
	public CommonResult<TriggerWrapper> getTrigger(JobNameParam param) {
		return CommonResult.putResult(jobOperatedService.getTrigger(param));
	}

	@Operation(summary = "Update trigger of job", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.TRIGGER)
	public CommonResult<Void> update(@RequestBody TriggerWrapper param) {
		return CommonResult.putResult(jobOperatedService.validateAndUpdateTrigger(param));
	}

	@Operation(summary = "Test cron trigger", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.TEST_CRON)
	public CommonResult<List<Date>> testCron(@RequestBody CronTriggerWrapper param) {
		return CommonResult.putResult(jobOperatedService.testCron(param));
	}

	@Operation(summary = "Test calendar offset trigger", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.TEST_CALENDAR_OFFSET)
	public CommonResult<List<Date>> testCalendarOffset(@RequestBody CalendarOffsetTriggerWrapper param) {
		return CommonResult.putResult(jobOperatedService.testCalendarOffset(param));
	}

	@Operation(summary = "Test daily time interval trigger", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.TEST_DAILY_TIME_INTERVAL)
	public CommonResult<List<Date>> testDailyTimeInterval(@RequestBody DailyTimeIntervalTriggerWrapper param) {
		return CommonResult.putResult(jobOperatedService.testDailyTimeInterval(param));
	}

	@Operation(summary = "Test calendar interval trigger", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.TEST_CALENDAR_INTERVAL)
	public CommonResult<List<Date>> testCalendarInterval(@RequestBody CalendarIntervalTriggerWrapper param) {
		return CommonResult.putResult(jobOperatedService.testCalendarInterval(param));
	}

	@Operation(summary = "List args of job", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.ARG)
	public CommonResult<Map<String, Object>> listArg(JobArgListParam param) {
		return CommonResult.putResult(jobService.listArg(param));
	}

	@Operation(summary = "Add arg", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.JobPath.ARG)
	public CommonResult<Void> addArg(@RequestBody JobArgAllocatedParam param) {
		return CommonResult.putResult(jobOperatedService.addArg(param));
	}

	@Operation(summary = "Remove arg", parameters = {@Parameter(name = "param", required = true)})
	@DeleteMapping(APIPath.JobPath.ARG)
	public CommonResult<Void> removeArg(@RequestBody JobArgAllocatedParam param) {
		return CommonResult.putResult(jobOperatedService.removeArg(param));
	}

	@Operation(summary = "Get json arg", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.JSON_ARG)
	public CommonResult<String> getJsonArg(JobNameParam param) {
		return CommonResult.putResult(jobService.getJsonArg(param));
	}

	@Operation(summary = "Get project", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.GET_PROJECT)
	public CommonResult<Project> getProject(JobNameParam param) {
		return CommonResult.putResult(jobService.getProject(param));
	}

	@Operation(summary = "Save project of job", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.SAVE_PROJECT)
	public CommonResult<Void> saveProject(@RequestBody JobProjectSaveParam param) {
		return CommonResult.putResult(jobService.saveProject(param));
	}

	@Operation(summary = "Get conditions by job", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.JobPath.GET_CONDITION)
	public CommonResult<ConditionResult> getCondition(JobNameParam param) {
		return CommonResult.putResult(jobService.getCondition(param));
	}

	@Operation(summary = "Update condition", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.JobPath.CONDITION)
	public CommonResult<Void> update(@RequestBody ConditionSaveParam param) {
		return CommonResult.putResult(jobService.updateCondition(param));
	}
}
