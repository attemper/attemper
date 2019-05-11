package com.github.attemper.sys.service;

public interface BaseService {

    /**
     * 从header中取出租户
     *
     * @return
     */
    String injectTenantId();

}
