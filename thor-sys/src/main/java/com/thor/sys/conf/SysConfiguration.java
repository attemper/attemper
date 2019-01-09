package com.thor.sys.conf;

import com.thor.sys.controller.UserController;
import com.thor.sys.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ldang
 */
@Configuration
@ComponentScan(basePackageClasses = {
        //config
        StarkClientConfig.class,

        //service
        UserService.class,

        //controller
        UserController.class

})
public class SysConfiguration {
}
