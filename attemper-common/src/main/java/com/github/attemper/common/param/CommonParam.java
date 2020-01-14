package com.github.attemper.common.param;

import com.github.attemper.java.sdk.common.param.BaseParam;

/**
 * common methods implemented by every param
 */
public interface CommonParam extends BaseParam {

    /**
     * handle some param before call controller
     */
    default void preHandle() {}

}
