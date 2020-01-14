package com.github.attemper.web.ext.app;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * handle interaction from web to executor
 */
@Service
@Transactional
public class ExecutorHandler extends CrossSystemHandler {

    @Autowired
    private AppProperties appProperties;

    public void terminate(Instance instance) {
        call(instance.getExecutorUri(), APIPath.ExecutorPath.TERMINATE, new IdParam().setId(instance.getId()));
    }

    public void load(String baseUrl, IdParam param) {
        call(baseUrl, APIPath.ExecutorPath.LOAD_PACKAGE, param);
    }

    public void unload(String baseUrl, IdParam param) {
        call(baseUrl, APIPath.ExecutorPath.UNLOAD_PACKAGE, param);
    }

    public void removeDataSource(String baseUrl, DataSourceNamesParam param) {
        call(baseUrl, APIPath.ExecutorPath.REMOVE_DATA_SOURCE, param);
    }

    private void call(String baseUrl, String apiSubPath, Object param) {
        CommonResult result = WebClient.builder().build()
                .method(HttpMethod.POST)
                .uri(buildFullPath(baseUrl, apiSubPath))
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstants.token, ServletUtil.getHeader(CommonConstants.token))
                .bodyValue(param)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .doOnError(Exception.class, err -> {
                    if (err instanceof RTException) {
                        throw (RTException) err;
                    } else if (err.getMessage().contains("Connection refused")) {
                        throw new RTException(3009, err);
                    }
                    throw new RTException(err);
                })
                .block();
        preHandleResult(result);
    }

    private String buildFullPath(String baseUrl, String apiPath) {
        return baseUrl + appProperties.getExecutor().getContextPath() + apiPath;
    }

}
