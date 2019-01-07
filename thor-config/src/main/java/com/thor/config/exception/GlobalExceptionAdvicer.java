package com.thor.config.exception;

import com.stark.sdk.common.exception.RTException;
import com.stark.sdk.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常捕获集中处理
 * @auth ldang
 */
@Slf4j
@RestControllerAdvice
@Order(1)
public class GlobalExceptionAdvicer {

	/**
	 * 自定义异常
	 * @param rte
	 * @return
	 */
	@ExceptionHandler(RTException.class)
	public CommonResult handleRTException(RTException rte) {
		return CommonResult.error(rte);
	}

}
