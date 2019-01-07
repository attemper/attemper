package com.thor.security.conf;

import com.thor.config.properties.StarkAppProperties;
import com.thor.security.filter.TokenFilter;
import com.thor.security.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        //conf
        RedisConfig.class,

        //filter
        XssFilter.class,

        //controller

        //service
})
public class SecurityConfiguration {

    @Bean
    public FilterRegistrationBean tokenFilterRegistrationBean(StarkAppProperties properties) {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new TokenFilter(properties));
        filterBean.getInitParameters().put("exclusions", "*.js,*.css,*.gif,*.jpg,*.png,*.html,*.map,*.woff,*.jpeg,*.tif,*.fiff");
        filterBean.addUrlPatterns("/api/*");
        return filterBean;
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
