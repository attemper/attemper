package com.github.attemper.security.xss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
public class XssFilter implements Filter {

	@Value("${filter.xss.enabled:true}")
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