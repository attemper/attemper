package com.thor.sys.conf;

import com.stark.sdk.common.constant.StarkSdkCommonConstants;
import com.stark.sdk.common.exception.RTException;
import com.stark.sdk.microservice.client.StarkClient;
import com.stark.sdk.microservice.client.StarkClientBuilder;
import com.stark.sdk.rest.context.StarkContext;
import com.stark.sdk.rest.handler.adapter.AfterHandlerAdapter;
import com.stark.sdk.rest.handler.adapter.PreHandlerAdapter;
import com.thor.config.properties.StarkAppProperties;
import com.thor.config.util.ServletUtil;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 多租户服务配置
 * @author ldang
 */
@Configuration
public class StarkClientConfig {

    @Bean
    public StarkClient starkClient (StarkAppProperties properties) {
        StarkClient starkClient = StarkClientBuilder
                .create()
                .serviceUrls(properties.getServiceUrls())
                .serviceName(properties.getServiceName())
                .tenantId(properties.getTenantId())
                .sign(properties.getSign())
                .addPreHandler(new CustomPreHandler())
                .addAfterHandler(new CustomAfterHandler())
                .build();
        return starkClient;
    }

    /**
     * 参数回调类
     */
    private class CustomPreHandler extends PreHandlerAdapter {
        @Override
        public void execute(HttpRequest httpRequest, StarkContext context) {
            String token = ServletUtil.getHeader(StarkSdkCommonConstants.token);
            if(token != null) {
                Header tokenHeader = new BasicHeader(StarkSdkCommonConstants.token, token);
                httpRequest.removeHeaders(StarkSdkCommonConstants.token);
                httpRequest.addHeader(tokenHeader);
                context.headers(Arrays.asList(httpRequest.getAllHeaders()));
            }
        }
    }

    /**
     * 返回值回调类
     */
    private class CustomAfterHandler extends AfterHandlerAdapter {
        @Override
        public void executeNot200(StarkContext context) {
            throw new RTException(context.getCommonResult().getCode(), context.getCommonResult().getMsg());
        }
    }
}
