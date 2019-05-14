package com.github.attemper.executor.task;

import com.github.attemper.executor.task.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor2biz.constant.Executor2BizAPIPath;
import com.github.attemper.java.sdk.common.result.execution.TaskResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Long Connection(Keep-Alive)
 */
public class SyncHttpTask extends HttpTask {

    @Override
    protected void executeIntern(WebClient webClient, DelegateExecution execution) {
        System.out.println("start execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName());
        TaskResult taskResult = super.invoke(webClient, execution, TaskResult.class);
        System.out.println("end execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName() + ":" + taskResult);
    }

    @Override
    protected String injectRouterPath() {
        return Executor2BizAPIPath.ROUTER_PATH_SYNC;
    }

    @Override
    protected int injectReadTimeout() {
        return 600;
    }
}
