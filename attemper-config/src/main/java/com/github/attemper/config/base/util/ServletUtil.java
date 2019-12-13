package com.github.attemper.config.base.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * servlet tool
 */
public class ServletUtil {
	
	/**
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static String getHeader(String name){
		return getRequest().getHeader(name);
	}
}
