package com.thor.security.shiro;

import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.config.bean.ContextBeanAware;
import com.thor.core.entity.Resource;
import com.thor.core.entity.User;
import com.thor.core.holder.UserHolder;
import com.thor.core.param.user.UserGetParam;
import com.thor.core.service.UserService;
import com.thor.security.ext.service.JWTService;
import com.thor.security.model.JWTToken;
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
            throw new RTException(ResultStatus.USER_NOTEXIST_ERROR); //1304
        }
        if(!dbUser.equals(user)){
            throw new RTException(ResultStatus.USER_USERNAME_OR_PASSWORD_ERROR); //1300
        }
        UserHolder.set(dbUser);
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
