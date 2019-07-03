package com.github.attemper.web.ext.app;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.dispatch.instance.JobInstanceIdParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.util.ServletUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * handle interaction from web to executor
 */
@Service
@Transactional
public class ExecutorHandler extends CrossSystemHandler {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebClient webClient;

    public void terminate(String baseUrl, JobInstance jobInstance) {
        call(buildFullPath(baseUrl, APIPath.ExecutorPath.TERMINATE), jobInstance);
    }

    public void pause(String baseUrl, JobInstance jobInstance) {
        call(buildFullPath(baseUrl, APIPath.ExecutorPath.PAUSE), jobInstance);
    }

    public void activate(String baseUrl, JobInstance jobInstance) {
        call(buildFullPath(baseUrl, APIPath.ExecutorPath.ACTIVATE), jobInstance);
    }

    private void call(String fullPath, JobInstance jobInstance) {
        if (StringUtils.isBlank(jobInstance.getRootProcInstId()) && StringUtils.isBlank(jobInstance.getProcInstId())) {
            return;
        }
        CommonResult result = webClient
                .method(HttpMethod.POST)
                .uri(fullPath)
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstants.token, ServletUtil.getHeader(CommonConstants.token))
                .syncBody(new JobInstanceIdParam().setId(jobInstance.getId()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .doOnError(WebClientResponseException.class, err -> {
                    if (err.getMessage() != null && err.getMessage().contains("Connection refused")) {
                        throw new RTException(3009, err.getMessage());
                    }
                    throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, err);
                })
                .block();
        preHandleResult(result);
    }

    private String buildFullPath(String baseUrl, String apiPath) {
        return baseUrl + appProperties.getExecutor().getContextPath() + apiPath;
    }

}
