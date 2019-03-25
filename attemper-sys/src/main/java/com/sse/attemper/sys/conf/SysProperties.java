package com.sse.attemper.sys.conf;

import com.sse.attemper.sdk.common.result.sys.tenant.Tenant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author ldang
 */
@ConfigurationProperties
@Getter
@Setter
public class SysProperties {

    @NestedConfigurationProperty
    private Tenant superTenant;

}
