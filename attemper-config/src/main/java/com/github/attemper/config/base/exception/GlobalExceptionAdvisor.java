package com.github.attemper.config.base.exception;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * centrally handling exception
 * @author ldang
 */
@Slf4j
@RestControllerAdvice
@Order(100)
public class GlobalExceptionAdvisor {

	/**
	 * customized exception: RTException
	 * @param rte
	 * @return
	 */
	@ExceptionHandler(RTException.class)
	public CommonResult handleRTException(RTException rte) {
		return CommonResult.error(rte);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public CommonResult handleDuplicateKeyException(DuplicateKeyException e) {
		return CommonResult.put(1200);
	}

}
