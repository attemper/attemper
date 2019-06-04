package com.github.attemper.executor.task;

import com.github.attemper.common.enums.JobInstanceStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.JobInstanceService;
import com.github.attemper.executor.task.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 * Short Connection with Async-Callback
 */
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
                execution.getActivityInstanceId().intern().wait(job.getTimeout() + 1800L);
                JobInstanceService jobInstanceService = SpringContextAware.getBean(JobInstanceService.class);
                JobInstanceAct jobInstanceAct = jobInstanceService.getAct(execution.getActivityInstanceId());
                if (jobInstanceAct.getStatus() == JobInstanceStatus.FAILURE.getStatus()) {
                    throw new RTException(3054);
                }
            } catch (InterruptedException e) {
                int code = 3053;
                saveInstanceAct(execution, null, String.valueOf(code), StatusProperty.getValue(code) + ":" + e.getMessage(), JobInstanceStatus.FAILURE);
                throw new RTException(code, e);
            }
        }
    }

    @Override
    protected String injectRouterPath() {
        return ExecutorAPIPath.ROUTER_PATH_ASYNC;
    }

}
