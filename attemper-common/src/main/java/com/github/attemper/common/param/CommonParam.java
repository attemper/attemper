package com.github.attemper.common.param;

/**
 * common methods implemented by every param
 * @author ldang
 */
public interface CommonParam {

    /**
     * validate param
     * @return
     */
    String validate();

    /**
     * handle some param before call controller
     */
    default void preHandle() {}

}
