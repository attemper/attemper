package com.github.attemper.invoker.service;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.config.base.property.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;

@Component
public class RetryHttpHandler {

    @Autowired
    private AppProperties appProperties;

    @Retryable(value = { PrematureCloseException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000L))
    public void invokeByWebClient(String baseUrl, String token, JobInvokingParam param) {
        WebClient.builder().build()
                .method(HttpMethod.POST)
                .uri(baseUrl + appProperties.getExecutor().getContextPath() + APIPath.ExecutorPath.JOB_INVOKING)
                .header(CommonConstants.token, token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(param)
                .retrieve()
                .onStatus(s -> s == HttpStatus.UNAUTHORIZED, resp -> Mono.error(new RTException(HttpStatus.UNAUTHORIZED.value(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .doOnError(Exception.class, err -> {
                    if (err instanceof RTException) {
                        throw (RTException) err;
                    } else if (err.getMessage().contains("Connection refused")) {
                        throw new RTException(3009, err);
                    }
                })
                .block();
    }
}
