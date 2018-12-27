package com.thor.security.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * XSS过滤
 * @author ldang
 */
@Configuration
public class XssFilter implements Filter {

	@Value("${filter.enabled.xss:true}")
	private boolean enabledXss;

	@Override
	public void init(FilterConfig filterConfig) {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (enabledXss) {
			request = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}