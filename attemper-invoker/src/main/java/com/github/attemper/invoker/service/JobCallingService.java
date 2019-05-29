package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.executor.JobInvokingResult;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.java.sdk.common.result.sys.login.LoginResult;
import com.github.attemper.security.service.LoginService;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.store.Store;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
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
        String baseUrl = instances.get(0).getUri() + optimizeContextPath(appProperties.getExecutor().getContextPath());
        String token = Store.getTenantTokenMap().get(tenantId);
        try {
            return invokeByWebClient(baseUrl, token, param);
        } catch (RTException e) {
            return invokeByWebClient(baseUrl, getToken(tenantId), param);
        }
    }

    private CommonResult invokeByWebClient(String baseUrl, String token, JobInvokingParam param) {
        return webClient
                .method(HttpMethod.POST)
                .uri(baseUrl + APIPath.ExecutorPath.JOB_INVOKING)
                .header(CommonConstants.token, token)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(param)
                .retrieve()
                .onStatus(s -> s == HttpStatus.UNAUTHORIZED, resp -> Mono.error(new RTException(HttpStatus.UNAUTHORIZED.value(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .block();
    }


    @Autowired
    private LoginService loginService;

    @Autowired
    private TenantService tenantService;

    private synchronized String getToken(String tenantId) {
        Tenant tenant = tenantService.get(TenantGetParam.builder().userName(tenantId).build());
        LoginResult loginResult = loginService.login(new LoginParam().setUserName(tenantId).setPassword(tenant.getPassword()));
        String token = loginResult.getToken();
        Store.getTenantTokenMap().put(tenantId, token);
        return token;
    }

    private String optimizeContextPath(String contextPath) {
        if (StringUtils.isBlank(contextPath)) {
            return "/";
        }
        contextPath = contextPath.trim();
        return contextPath.startsWith("/") ? contextPath : "/" + contextPath;
    }
}
