package com.github.attemper.executor.task.http;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.executor.store.ExecutionStore;
import com.github.attemper.executor.task.http.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
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
        super.invoke(execution, job, url, LogResult.class);
        synchronized (execution.getActivityInstanceId().intern()) { // lock
            try {
                execution.getActivityInstanceId().intern().wait(job.getTimeout() * 1000L);
                EndParam endParam = ExecutionStore.getEndMap().get(execution.getId());
                TaskResult taskResult = endParam.getTaskResult();
                if (taskResult != null) {
                    saveVariables(execution, taskResult);
                    if (!taskResult.getSuccess()) {
                        saveLogKey(execution, taskResult.getLogKey());
                        throw new RTException(taskResult.getLogText());
                    }
                }
            } catch (InterruptedException e) {
                int code = 3053;
                saveLogKey(execution, String.valueOf(code));
                throw new RTException(code, e);
            } catch (Exception e) {
                int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
                saveLogKey(execution, String.valueOf(code));
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
