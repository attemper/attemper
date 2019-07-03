package com.github.attemper.security.shiro;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.security.model.JWTToken;
import com.github.attemper.sys.ext.service.JWTService;
import com.github.attemper.sys.holder.TenantHolder;
import com.github.attemper.sys.service.TenantService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 域
 * @author ldang
 */
public class CustomRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 认证
     * @param auth
     * @return
     * @throws UnauthorizedException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        Tenant tenant = SpringContextAware.getBean(JWTService.class).parseToken(token);
        Tenant dbTenant = SpringContextAware.getBean(TenantService.class).get(new TenantGetParam().setUserName(tenant.getUserName()));
        if (dbTenant == null) {
            throw new RTException(1303);
        }
        TenantHolder.set(dbTenant);
        return new SimpleAuthenticationInfo(tenant, token, getName());
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Tenant tenant = (Tenant) principals.getPrimaryPrincipal();
        Set<String> resourcePermissions = new HashSet<>();
        TenantService tenantService = SpringContextAware.getBean(TenantService.class);
        List<String> resources = tenantService.getResources(new TenantGetParam().setUserName(tenant.getUserName()));
        resources.forEach(resource -> resourcePermissions.add(resource));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(resourcePermissions);
        return simpleAuthorizationInfo;
    }

}
