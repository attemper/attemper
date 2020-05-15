package com.github.attemper.security.conf;

import com.github.attemper.java.sdk.common.constant.SdkAPIPath;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.security.shiro.CustomRealm;
import com.github.attemper.security.shiro.JWTFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean
    public SecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(new CustomRealm());
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        String jwtFilter = "jwtFilter";
        filters.put(jwtFilter, new JWTFilter());
        bean.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put(SdkAPIPath.LoginPath.$, "anon");  //web login
        filterMap.put(SdkAPIPath.LoginPath.LOGIN_BY_ENCODED_USERNAME_PWD, "anon");  //sdk login
        filterMap.put("/swagger-ui.html", "anon");  //swagger-ui
        filterMap.put("/swagger-ui/**", "anon");  //swagger-ui
        filterMap.put("/v3/api-docs/**", "anon");  //swagger-ui
        //filterMap.put("/kaptcha", "anon");
        filterMap.put(ExecutorAPIPath.RouterPath.SYNC, "anon");
        filterMap.put(ExecutorAPIPath.RouterPath.ASYNC, "anon");
        filterMap.put("/**", jwtFilter);
        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        proxyCreator.setUsePrefix(true);
        return proxyCreator;
    }
}
