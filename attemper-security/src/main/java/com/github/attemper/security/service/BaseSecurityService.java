package com.github.attemper.security.service;

import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.common.result.sys.user.User;
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
