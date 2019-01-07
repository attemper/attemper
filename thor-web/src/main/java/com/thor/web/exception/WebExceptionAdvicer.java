package com.thor.web.exception;

import com.stark.sdk.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auth: ldang
 */
@Slf4j
@RestControllerAdvice
@Order(100)
public class WebExceptionAdvicer {

    /**
     * 主键重复异常
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResult handleDuplicateKeyException(DuplicateKeyException e) {
        return CommonResult.put(1200);
    }

    /**
     * Exception异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return CommonResult.error(e.getMessage());
    }
}
