package com.thor.sys.conf;

import com.thor.sys.controller.ResourceController;
import com.thor.sys.controller.TagController;
import com.thor.sys.controller.TenantController;
import com.thor.sys.controller.UserController;
import com.thor.sys.init.CustomPostConstruct;
import com.thor.sys.service.ResourceService;
import com.thor.sys.service.TagService;
import com.thor.sys.service.TenantService;
import com.thor.sys.service.UserService;
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
