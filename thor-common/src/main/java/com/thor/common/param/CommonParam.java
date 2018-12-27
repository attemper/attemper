package com.thor.common.param;

/**
 * 基础参数
 * @author ldang
 */
public interface CommonParam {

    /**
     * 在hibernate-validator无法校验时，做补充校验
     * @return
     */
    default String validate() {
        return null;
    }

    /**
     * 预处理参数
     */
    default void preHandle() {

    }

}
