package com.thor.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author ldang
 */
@Data
@ConfigurationProperties(prefix = "stark")
public class StarkAppProperties {

    /** 多租户平台的eureka地址 */
    private String eurekaZone;

    /** 多租户平台的服务地址列表 */
    private List<String> serviceUrls;

    /**
     * 多租户平台的服务名
     */
    private String serviceName;

    /**
     * 使用的租户编号
     */
    private String tenantId;

    /**
     * 使用的租户的静态签名
     */
    private String sign;
}
