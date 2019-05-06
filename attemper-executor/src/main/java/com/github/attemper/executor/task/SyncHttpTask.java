package com.github.attemper.executor.task;

import com.github.attemper.executor.task.internal.HttpTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Long Connection(Keep-Alive)
 */
public class SyncHttpTask extends HttpTask {

    @Override
    protected void executeWithUrl(String url, String jobName, DelegateExecution execution) {
        System.out.println("start execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName());
        WebClient webClient = super.buildWebClient(url, 600);
        String result = super.invoke(webClient, execution);
        System.out.println("end execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName() + result);
    }

    @Override
    protected Map<String, Object> buildParamMap(DelegateExecution execution) {
        return execution.getVariables();
    }
}
