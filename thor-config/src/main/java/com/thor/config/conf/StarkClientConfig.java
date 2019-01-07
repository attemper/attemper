package com.thor.config.conf;

import com.stark.sdk.common.exception.RTException;
import com.stark.sdk.common.result.CommonResult;
import com.stark.sdk.microservice.client.StarkClient;
import com.stark.sdk.rest.support.adapter.AfterHandlerAdapter;
import com.thor.config.properties.StarkAppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ldang
 */
@Configuration
public class StarkClientConfig {

    @Bean
    public StarkClient starkClient (StarkAppProperties properties) {
        StarkClient starkClient = new StarkClient(properties.getServiceUrls(), properties.getServiceName(),
                properties.getTenantId(), properties.getSign());
        starkClient.registerAfterHandler(new CustomAfterHandler());
        return starkClient;
    }

    /**
     * 返回值回调类
     */
    private class CustomAfterHandler extends AfterHandlerAdapter {
        @Override
        public void executeNot200(CommonResult commonResult) {
            throw new RTException(commonResult.getCode(), commonResult.getMsg());
        }
    }
}
