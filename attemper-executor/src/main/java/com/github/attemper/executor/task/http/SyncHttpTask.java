package com.github.attemper.executor.task.http;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.task.http.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Long Connection(Keep-Alive)
 */
@Component
public class SyncHttpTask extends HttpTask {

    @Override
    protected void executeIntern(DelegateExecution execution, String url, Map<String, String> fieldMap) {
        TaskResult taskResult = super.invoke(execution, url, fieldMap, TaskResult.class);
        if (taskResult != null && !taskResult.getSuccess()) {
            saveLogKey(execution, taskResult.getLogKey());
            saveVariables(execution, taskResult);
            throw new RTException(taskResult.getLogText());
        }
    }

    @Override
    protected String injectRouterPath() {
        return ExecutorAPIPath.RouterPath.SYNC;
    }
}
