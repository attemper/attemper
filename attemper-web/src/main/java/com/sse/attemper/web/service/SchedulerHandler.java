package com.sse.attemper.web.service;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.scheduler.TriggerChangedParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.property.AppProperties;
import com.sse.attemper.sys.holder.TenantHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * handle interaction from web to scheduler
 */
@Slf4j
@Service
@Transactional
public class SchedulerHandler {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private DiscoveryClient discoveryClient;

    public CommonResult<Void> updateTrigger(TriggerChangedParam param) {
        List<String> urls = buildUrls(APIPath.SchedulerPath.TRIGGER);
        List<Callable<CommonResult>> callers = new ArrayList<>(urls.size());
        urls.forEach(url -> callers.add(new SchedulerCaller(HttpMethod.PUT, url, param, TenantHolder.get())));
        return invokeAll(callers);
    }

    private CommonResult<Void> invokeAll(List<Callable<CommonResult>> callers) {
        if (callers.isEmpty()) {
            CommonResult<Void> commonResult = CommonResult.put(3000);
            log.error(commonResult.getMsg());
            return commonResult;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(callers.size());
        try {
            List<Future<CommonResult>> futures = executorService.invokeAll(callers);
            for (Future<CommonResult> item : futures) {
                preHandleResult(item.get());
            }
            executorService.shutdown();
            return CommonResult.ok();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Connection refused")) {
                CommonResult<Void> commonResult = CommonResult.put(3001);
                log.error(commonResult.getMsg());
                return commonResult;
            }
            throw new RTException(500, e);
        }
    }

    private List<String> buildUrls(String uri) {
        List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getScheduler().getName());
        List<String> urls = new ArrayList<>(instances.size());
        instances.forEach(item -> urls.add(item.getUri() + "/" + appProperties.getScheduler().getContextPath() + uri));
        return urls;
    }

    private void preHandleResult(CommonResult commonResult) {
        if (commonResult.getCode() != CommonConstants.OK) {
            throw new RTException(commonResult.getCode(), commonResult.getMsg());
        }
    }

    private class SchedulerCaller implements Callable<CommonResult> {

        private HttpMethod method;

        private String url;

        private Object param;

        private Tenant adminedTenant;

        public SchedulerCaller(HttpMethod method, String url, Object param, Tenant adminedTenant) {
            this.method = method;
            this.url = url;
            this.param = param;
            this.adminedTenant = adminedTenant;
        }

        @Override
        public CommonResult call() throws Exception {
            return ContextBeanAware.getBean(WebClient.class)
                    .method(method)
                    .uri(url)
                    //.accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(CommonConstants.tenantId, adminedTenant.getId())
                    .header(CommonConstants.sign, adminedTenant.getSign())
                    .syncBody(param)
                    .retrieve()
                    .bodyToMono(CommonResult.class)
                    .block();
        }
    }
}
