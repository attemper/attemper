package com.sse.attemper.security.shiro;

import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.sys.user.UserGetParam;
import com.sse.attemper.common.result.sys.resource.Resource;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.security.ext.service.JWTService;
import com.sse.attemper.security.model.JWTToken;
import com.sse.attemper.sys.holder.TenantHolder;
import com.sse.attemper.sys.holder.UserHolder;
import com.sse.attemper.sys.service.UserService;
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
        User user = ContextBeanAware.getBean(JWTService.class).parseTokenToUser(token);
        UserService userService = ContextBeanAware.getBean(UserService.class);
        User dbUser = userService.get(new UserGetParam(user.getUserName()));
        if(dbUser == null){
            throw new RTException(1303);
        }
        if(!dbUser.equals(user)){
            throw new RTException(1300); //1300
        }
        UserHolder.set(dbUser);
        TenantHolder.set(userService.getAdminedTenant());
        return new SimpleAuthenticationInfo(user, token, getName());
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        Set<String> resourcePermissions = new HashSet<>();
        UserService userService = ContextBeanAware.getBean(UserService.class);
        List<Resource> resources = userService.getResources(new UserGetParam(user.getUserName()));
        resources.forEach(resource -> resourcePermissions.add(resource.getResourceName()));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(resourcePermissions);
        return simpleAuthorizationInfo;
    }

}
