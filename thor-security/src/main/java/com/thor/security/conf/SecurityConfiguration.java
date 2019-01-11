package com.thor.security.conf;

import com.thor.security.controller.LoginController;
import com.thor.security.service.LoginService;
import com.thor.security.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        //filter
        XssFilter.class,

        //conf
        RedisConfig.class,

        //service
        LoginService.class,

        //controller
        LoginController.class

})
public class SecurityConfiguration {

    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new XssFilter());
        filter.setOrder(Integer.MAX_VALUE);
        filter.addUrlPatterns("/*");
        return filter;
    }
}
