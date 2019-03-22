package com.sse.attemper.sys.conf;

import com.sse.attemper.sys.controller.ResourceController;
import com.sse.attemper.sys.controller.TagController;
import com.sse.attemper.sys.controller.TenantController;
import com.sse.attemper.sys.controller.UserController;
import com.sse.attemper.sys.init.CustomPostConstruct;
import com.sse.attemper.sys.service.ResourceService;
import com.sse.attemper.sys.service.TagService;
import com.sse.attemper.sys.service.TenantService;
import com.sse.attemper.sys.service.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ldang
 */
@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@ComponentScan(basePackageClasses = {
        //config

        //service
        TenantService.class,
        UserService.class,
        ResourceService.class,
        TagService.class,

        //controller
        TenantController.class,
        ResourceController.class,
        UserController.class,
        TagController.class,

        //init
        CustomPostConstruct.class

})
public class SysConfiguration {
}
