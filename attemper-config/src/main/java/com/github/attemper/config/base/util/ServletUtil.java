package com.github.attemper.config.base.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet tool
 * @author ldang
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
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static String getHeader(String name){
		return getRequest().getHeader(name);
	}
	
	/**
	 * 从HttpServletRequest中获取参数
	 * @param name
	 * @return
	 */
	public static String getParameter(String name) {
		HttpServletRequest request = getRequest();
		String value = request.getParameter(name);
		if(value == null) {
			Object object = request.getAttribute(name);
			if(object instanceof String && object != null) {
				return (String) object;
			}
			return null;
		}
		return value;
	}
	
	/**
	 * 服务端设置attribute
	 * @param name
	 * @param value
	 */
	public static void setAttribute(String name, String value) {
		getRequest().setAttribute(name, value);
	}

	/**
	 * 获取服务端设置的attribute
	 * @param name
	 * @return
	 */
	public static Object getAttribute(String name) {
		return getRequest().getAttribute(name);
	}
}
