package com.github.attemper.executor.task.http;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.store.ExecutorStore;
import com.github.attemper.executor.task.http.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Short Connection with Async-Callback
 */
@Component
public class AsyncHttpTask extends HttpTask {

    @Override
    protected void executeIntern(DelegateExecution execution, String url, Map<String, String> fieldMap) {
        super.invoke(execution, url, fieldMap, LogResult.class);
        synchronized (execution.getActivityInstanceId().intern()) { // lock
            try {
                execution.getActivityInstanceId().intern().wait(injectTimeout(fieldMap) * 1000L);
                EndParam endParam = ExecutorStore.getEndMap().get(execution.getId());
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
                saveLogKey(execution, code);
                throw new RTException(code, e);
            } catch (Exception e) {
                int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
                saveLogKey(execution, code);
                throw new RTException(code, e);
            } finally {
                ExecutorStore.getEndMap().remove(execution.getId());
            }
        }
    }

    @Override
    protected String injectRouterPath() {
        return ExecutorAPIPath.RouterPath.ASYNC;
    }

}
