package com.github.attemper.config.base.exception;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常捕获集中处理
 * @auth ldang
 */
@Slf4j
@RestControllerAdvice
@Order(100)
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

	/**
	 * 主键重复异常
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	public CommonResult handleDuplicateKeyException(DuplicateKeyException e) {
		return CommonResult.put(1200);
	}

	/**
	 * Exception异常
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public CommonResult handleException(Exception e) {
		log.error(e.getMessage(), e);
		return CommonResult.error(e.getMessage());
	}
}
