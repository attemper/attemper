package com.thor.sys.service;

public interface BaseService {

    /*    *//**
     * 在service中注入用户
     * @return
     *//*
    User injectUser();

    */

    /**
     * 从header中取出租户
     * @return
     */
    String injectTenantId();
}
