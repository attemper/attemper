package com.github.attemper.executor.task;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.executor.task.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 * Long Connection(Keep-Alive)
 */
public class SyncHttpTask extends HttpTask {

    @Override
    protected void executeIntern(DelegateExecution execution, Job job, String url) {
        TaskResult taskResult = super.invoke(execution, job, url, TaskResult.class);
        if (taskResult != null) {
            saveInstanceAct(execution, url, taskResult.getLogKey(), taskResult.getLogText(),
                    taskResult.getSuccess() ? JobInstanceStatus.SUCCESS : JobInstanceStatus.FAILURE);
        } else {
            saveInstanceAct(execution, url, null, null, JobInstanceStatus.SUCCESS);
        }
    }

    @Override
    protected String injectRouterPath() {
        return ExecutorAPIPath.ROUTER_PATH_SYNC;
    }
}
