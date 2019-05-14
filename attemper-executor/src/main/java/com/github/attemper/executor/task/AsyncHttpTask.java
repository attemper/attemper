package com.github.attemper.executor.task;

import com.github.attemper.executor.task.internal.HttpTask;
import com.github.attemper.java.sdk.common.executor2biz.constant.Executor2BizAPIPath;
import com.github.attemper.java.sdk.common.result.execution.LogResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Short Connection with Async-Callback
 */
public class AsyncHttpTask extends HttpTask {

    @Override
    protected void executeIntern(WebClient webClient, DelegateExecution execution) {
        System.out.println("start execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName());
        LogResult logResult = super.invoke(webClient, execution, LogResult.class);
        System.out.println(logResult);
        synchronized (execution.getActivityInstanceId().intern()) { // lock
            try {
                execution.getActivityInstanceId().intern().wait(120000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName());
    }

    @Override
    protected String injectRouterPath() {
        return Executor2BizAPIPath.ROUTER_PATH_ASYNC;
    }

    @Override
    protected int injectReadTimeout() {
        return 300;
    }
}
