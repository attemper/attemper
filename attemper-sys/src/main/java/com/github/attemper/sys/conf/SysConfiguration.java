package com.github.attemper.sys.conf;

import com.github.attemper.config.base.conf.ConfigConfiguration;
import com.github.attemper.sys.controller.ResourceController;
import com.github.attemper.sys.controller.TagController;
import com.github.attemper.sys.controller.TenantController;
import com.github.attemper.sys.controller.UserController;
import com.github.attemper.sys.dao.mapper.ResourceMapper;
import com.github.attemper.sys.dao.mapper.TagMapper;
import com.github.attemper.sys.dao.mapper.TenantMapper;
import com.github.attemper.sys.dao.mapper.UserMapper;
import com.github.attemper.sys.ext.service.SecretService;
import com.github.attemper.sys.init.CustomPostConstruct;
import com.github.attemper.sys.service.ResourceService;
import com.github.attemper.sys.service.TagService;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.service.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ldang
 */
@Configuration
@EnableConfigurationProperties(SysProperties.class)
@Import({
        CustomPostConstruct.class,
        ConfigConfiguration.class
})
@ComponentScan(basePackageClasses = {
        //service
        TenantService.class,
        UserService.class,
        ResourceService.class,
        TagService.class,
        SecretService.class,

        //controller
        TenantController.class,
        ResourceController.class,
        UserController.class,
        TagController.class,

        TenantMapper.class,
        ResourceMapper.class,
        UserMapper.class,
        TagMapper.class
})
public class SysConfiguration {
}
