package com.github.attemper.web.ext.app;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.param.dispatch.instance.JobInstanceIdParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.util.ServletUtil;
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
        if (jobInstance.getProcInstId() == null) {
            return;
        }
        call(baseUrl, APIPath.ExecutorPath.TERMINATE, new JobInstanceIdParam().setId(jobInstance.getId()));
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
        CommonResult result = webClient
                .method(HttpMethod.POST)
                .uri(buildFullPath(baseUrl, apiSubPath))
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstants.token, ServletUtil.getHeader(CommonConstants.token))
                .syncBody(param)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(new RTException(resp.rawStatusCode(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .doOnError(WebClientResponseException.class, err -> {
                    if (err.getMessage() != null && err.getMessage().contains("Connection refused")) {
                        throw new RTException(3009, err.getMessage());
                    }
                    throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), err);
                })
                .block();
        preHandleResult(result);
    }

    private String buildFullPath(String baseUrl, String apiPath) {
        return baseUrl + appProperties.getExecutor().getContextPath() + apiPath;
    }

}
