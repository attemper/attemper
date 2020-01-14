package com.github.attemper.sys.conf;

import com.github.attemper.config.base.conf.ConfigConfiguration;
import com.github.attemper.sys.aspect.ControllerAspect;
import com.github.attemper.sys.controller.RoleController;
import com.github.attemper.sys.controller.TenantController;
import com.github.attemper.sys.dao.RoleMapper;
import com.github.attemper.sys.dao.TenantMapper;
import com.github.attemper.sys.exception.SysExceptionAdvisor;
import com.github.attemper.sys.service.RoleService;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.store.SysStore;
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
        RoleMapper.class,

        //service
        TenantService.class,
        RoleService.class,

        //controller
        TenantController.class,
        RoleController.class,
})
public class SysConfiguration {

    @Autowired
    private TenantService tenantService;

    @PostConstruct
    public void initAdminTenant() {
        SysStore.setAdminTenant(tenantService.getAdmin());
    }
}
