package com.github.attemper.web.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.param.scheduler.TriggerChangedParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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
public class SchedulerHandler extends CrossSystemHandler {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private DiscoveryClient discoveryClient;

    public CommonResult<Void> saveCalendar(CalendarGetParam param) {
        return call(APIPath.SchedulerPath.CALENDAR, param);
    }

    public CommonResult<Void> updateTrigger(TriggerChangedParam param) {
        return call(APIPath.SchedulerPath.TRIGGER, param);
    }

    private CommonResult<Void> call(String apiPath, Object param) {
        if (appProperties.getWeb().isEnableScheduling()) {
            if (log.isDebugEnabled()) {
                log.debug("Web is enable-scheduling");
            }
            return null;
        }
        List<String> urls = buildUrls(apiPath);
        List<Callable<CommonResult>> callers = new ArrayList<>(urls.size());
        urls.forEach(url -> callers.add(new SchedulerCaller(
                HttpMethod.POST,
                url,
                ServletUtil.getHeader(CommonConstants.token),
                param)));
        return invokeAll(callers);
    }

    private CommonResult<Void> invokeAll(List<Callable<CommonResult>> callers) {
        if (callers.isEmpty()) {
            throw new RTException(3000);
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
                throw new RTException(3001, e.getMessage());
            }
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, e);
        }
    }

    private List<String> buildUrls(String uri) {
        List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getScheduler().getName());
        List<String> urls = new ArrayList<>(instances.size());
        instances.forEach(item -> urls.add(item.getUri() + appProperties.getScheduler().getContextPath() + uri));
        return urls;
    }

    private class SchedulerCaller implements Callable<CommonResult> {

        private HttpMethod method;

        private String url;

        private String token;

        private Object param;

        public SchedulerCaller(HttpMethod method, String url, String token, Object param) {
            this.method = method;
            this.url = url;
            this.token = token;
            this.param = param;
        }

        @Override
        public CommonResult call() throws Exception {
            return SpringContextAware.getBean(WebClient.class)
                    .method(method)
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(CommonConstants.token, token)
                    .syncBody(param)
                    .retrieve()
                    .bodyToMono(CommonResult.class)
                    .block();
        }
    }
}
