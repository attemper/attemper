package com.github.attemper.sys.exception;

import com.github.attemper.common.result.CommonResult;
import com.github.attemper.java.sdk.common.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * centrally handling exception
 * @author ldang
 */
@Slf4j
@RestControllerAdvice
@Order(200)
public class SysExceptionAdvisor {

	@ExceptionHandler(UnauthorizedException.class)
	public CommonResult handleUnauthorizedException(UnauthorizedException e) {
		return CommonResult.put(1010);
	}

	@ExceptionHandler(Exception.class)
	public CommonResult handleException(Exception e) {
		log.error(e.getMessage(), e);
		return CommonResult.error(ExceptionUtil.getStackTrace(e));
	}

}
