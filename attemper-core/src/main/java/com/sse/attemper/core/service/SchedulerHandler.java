package com.sse.attemper.core.service;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.dispatch.job.BaseJobRemoveParam;
import com.sse.attemper.common.param.scheduler.TriggerChangedParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.config.property.AppProperties;
import com.sse.attemper.sys.holder.TenantHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * handle interaction from web to scheduler
 */
@Service
@Transactional
public class SchedulerHandler {

    @Autowired
    private AppProperties appProperties;

    /*
    @Autowired
    private DiscoveryClient discoveryClient;
    */

    @Autowired
    private WebClient webClient;

    public void deleteJob(BaseJobRemoveParam param) {
        execute(HttpMethod.DELETE, APIPath.SchedulerPath.JOB, param);
    }

    public void updateTrigger(TriggerChangedParam param) {
        execute(HttpMethod.PUT, APIPath.SchedulerPath.TRIGGER, param);
    }

    private CommonResult execute(HttpMethod httpMethod, String apiPath, Object param) {
        CommonResult commonResult = webClient.method(httpMethod)
                .uri(buildUri(apiPath))
                //.accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstants.tenantId, TenantHolder.get().getId())
                .header(CommonConstants.sign, TenantHolder.get().getSign())
                .syncBody(param)
                .retrieve()
                .bodyToMono(CommonResult.class)
                .block();
        preHandleResult(commonResult);
        return commonResult;
    }

    private void preHandleResult(CommonResult commonResult) {
        if (commonResult.getCode() != CommonConstants.OK) {
            throw new RTException(commonResult.getCode(), commonResult.getMsg());
        }
    }

    private String buildUri(String apiPath) {
        return "http://" + appProperties.getScheduler().getName() + "/"
                + appProperties.getScheduler().getContextPath() + apiPath;
    }
}
