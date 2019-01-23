package com.thor.security.conf;

import com.thor.security.controller.LoginController;
import com.thor.security.ext.service.JWTService;
import com.thor.security.service.LoginService;
import com.thor.security.xss.XssFilter;
import com.thor.sys.ext.service.SecretService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
@ComponentScan(basePackageClasses = {
        //conf
        ShiroConfig.class,

        //jwt
        JWTService.class,

        //filter
        XssFilter.class,

        //service
        SecretService.class,
        LoginService.class,

        //controller
        LoginController.class,

})
public class SecurityConfiguration {

    @Bean
    public FilterRegistrationBean shiroFilterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filter.setFilter(proxy);
        filter.getInitParameters().put("exclusions", "*.js,*.css,*.gif,*.jpg,*.png,*.html,*.map,*.woff,*.jpeg,*.tif,*.fiff");
        filter.setOrder(Integer.MAX_VALUE - 1);
        filter.addUrlPatterns("/*");
        return filter;
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new XssFilter());
        filter.setOrder(Integer.MAX_VALUE);
        filter.addUrlPatterns("/*");
        return filter;
    }
}
