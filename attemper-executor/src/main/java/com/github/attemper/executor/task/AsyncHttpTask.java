package com.github.attemper.executor.task;

import com.github.attemper.executor.task.internal.HttpTask;
import com.github.attemper.executor.constant.ParamConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Short Connection with Async-Callback
 */
public class AsyncHttpTask extends HttpTask {

    @Override
    protected void executeWithUrl(String url, String jobName, DelegateExecution execution) {
        System.out.println("start execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName());
        WebClient webClient = super.buildWebClient(url, 300);
        String result = super.invoke(webClient, execution);
        System.out.println(result);
        synchronized (execution.getId().intern()) {
            try {
                execution.getId().intern().wait(120000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end execution:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":" + execution.getCurrentActivityName());
    }

    @Override
    protected Map<String, Object> buildParamMap(DelegateExecution execution) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ParamConstants.executionId, execution.getId());
        paramMap.put(ParamConstants.bizParam, execution.getVariables());
        return paramMap;
    }
}
