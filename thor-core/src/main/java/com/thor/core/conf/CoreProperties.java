package com.thor.core.conf;

import com.thor.core.entity.Tenant;
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
public class CoreProperties {

    @NestedConfigurationProperty
    private Tenant superTenant;

}
