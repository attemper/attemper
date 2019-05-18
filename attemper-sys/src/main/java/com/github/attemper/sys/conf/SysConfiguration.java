package com.github.attemper.sys.conf;

import com.github.attemper.config.base.conf.ConfigConfiguration;
import com.github.attemper.sys.aspect.ControllerAspect;
import com.github.attemper.sys.controller.TagController;
import com.github.attemper.sys.controller.TenantController;
import com.github.attemper.sys.dao.mapper.TagMapper;
import com.github.attemper.sys.dao.mapper.TenantMapper;
import com.github.attemper.sys.exception.SysExceptionAdvisor;
import com.github.attemper.sys.ext.service.SecretService;
import com.github.attemper.sys.service.TagService;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.store.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Configuration
@Import({
        ConfigConfiguration.class
})
@ComponentScan(basePackageClasses = {
        SysExceptionAdvisor.class,

        ControllerAspect.class,

        //mapper
        TenantMapper.class,
        TagMapper.class,

        //service
        TenantService.class,
        TagService.class,
        SecretService.class,

        //controller
        TenantController.class,
        TagController.class,
})
public class SysConfiguration {

    @Autowired
    private TenantService tenantService;

    @PostConstruct
    public void initAdminTenant() {
        Store.setAdminTenant(tenantService.getAdmin());
    }
}
