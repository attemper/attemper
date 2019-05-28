package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.executor.JobInvokingResult;
import com.github.attemper.config.base.property.AppProperties;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class JobCallingService {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private WebClient webClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    public CommonResult<JobInvokingResult> invoke(String jobName, String tenantId) {
        return this.invoke(jobName, null, tenantId);
    }

    public CommonResult<JobInvokingResult> invoke(String jobName, String triggerName, String tenantId) {
        JobInvokingParam param = JobInvokingParam.builder()
                .id(idGenerator.getNextId())
                .jobName(jobName)
                .triggerName(triggerName)
                .tenantId(tenantId)
                .build();
        List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getExecutor().getName());
        if (instances.size() == 0) {
            throw new RTException(500);
        }
        String baseUrl = instances.get(0).getUri() + "/" + appProperties.getExecutor().getContextPath();
        return webClient
                .method(HttpMethod.POST)
                .uri(baseUrl + APIPath.ExecutorPath.JOB_INVOKING)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(param)
                .retrieve()
                .bodyToMono(CommonResult.class)
                .block();
    }
}
