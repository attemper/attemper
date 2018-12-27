package com.thor.security.service;

import com.thor.core.entity.User;
import com.thor.core.service.BaseServiceAdapter;
import org.apache.shiro.SecurityUtils;

/**
 * security中的基础service
 * @author ldang
 */
public class BaseSecurityService extends BaseServiceAdapter {

    /**
     * 从shiro中获取用户<br>
     * 覆盖从UserHolder获取User
     * @return
     */
    @Override
    public User injectUser(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}
