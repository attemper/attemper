package com.github.attemper.executor.task.http;

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
        appendLogText(execution, 10105);
        TaskResult taskResult = super.invoke(execution, url, fieldMap, TaskResult.class);
        afterExecution(taskResult, execution);
    }

    @Override
    protected String injectRouterPath() {
        return ExecutorAPIPath.RouterPath.SYNC;
    }
}
