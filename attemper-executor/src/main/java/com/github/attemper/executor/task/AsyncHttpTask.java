package com.github.attemper.executor.task;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.executor.store.ExecutionStore;
import com.github.attemper.executor.task.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Short Connection with Async-Callback
 */
@Component
public class AsyncHttpTask extends HttpTask {

    @Override
    protected void executeIntern(DelegateExecution execution, Job job, String url) {
        LogResult logResult = super.invoke(execution, job, url, LogResult.class);
        if (logResult != null) {
            int code = 3052;
            if (!logResult.getSuccess()) {
                saveInstanceAct(execution, url,
                        code + (StringUtils.isBlank(logResult.getLogKey()) ? "" : "," + logResult.getLogKey()),
                        StatusProperty.getValue(code) + (StringUtils.isBlank(logResult.getLogText()) ? "" :
                                ":" + logResult.getLogText()),
                        JobInstanceStatus.FAILURE);
                throw new RTException(code);
            } else {
                saveInstanceAct(execution, url, logResult.getLogKey(), logResult.getLogText(), null);
            }
        } else {
            saveInstanceAct(execution, url, null, null, null);
        }
        synchronized (execution.getActivityInstanceId().intern()) { // lock
            try {
                execution.getActivityInstanceId().intern().wait(job.getTimeout() * 1000L);
                EndParam endParam = ExecutionStore.getEndMap().get(execution.getId());
                TaskResult taskResult = endParam.getTaskResult();
                if (taskResult != null) {
                    saveVariables(execution, taskResult);
                    if (taskResult.getSuccess()) {
                        saveInstanceAct(execution, null, taskResult.getLogKey(), taskResult.getLogText(), JobInstanceStatus.SUCCESS);
                    } else {
                        saveInstanceAct(execution, null, taskResult.getLogKey(), taskResult.getLogText(), JobInstanceStatus.FAILURE);
                        int code = 3054;
                        saveInstance(execution, String.valueOf(code), StatusProperty.getValue(code), JobInstanceStatus.FAILURE);
                        throw new RTException(code);
                    }
                } else {
                    saveInstanceAct(execution, null, null, null, JobInstanceStatus.SUCCESS);
                }
            } catch (InterruptedException e) {
                int code = 3053;
                saveInstanceAct(execution, null, String.valueOf(code), StatusProperty.getValue(code) + ":" + e.getMessage(), JobInstanceStatus.FAILURE);
                throw new RTException(code, e);
            } catch (Exception e) {
                int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
                saveInstanceAct(execution, null, String.valueOf(code), StatusProperty.getValue(code) + ":" + e.getMessage(), JobInstanceStatus.FAILURE);
                throw new RTException(code, e);
            } finally {
                ExecutionStore.getEndMap().remove(execution.getId());
            }
        }
    }

    @Override
    protected String injectRouterPath() {
        return ExecutorAPIPath.RouterPath.ASYNC;
    }

}
