package com.thor.security.filter;

import com.thor.common.constant.CommonConstants;
import com.thor.config.properties.StarkAppProperties;
import com.thor.config.util.ServletUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录及获取Token的过滤器
 * @author ldang
 */
public class TokenFilter extends OncePerRequestFilter {

    protected final StarkAppProperties properties;

    public TokenFilter(StarkAppProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = ServletUtil.getHeader(CommonConstants.token);
        if (token == null) {
            redirectOrSendNotPermitted(request, response);
        }
    }

    /**
     * 跳转到多租户平台来认证
     * @param request
     * @param response
     */
    protected void redirectOrSendNotPermitted(HttpServletRequest request, HttpServletResponse response) {
        if(isRootPath(request)) {
            redirectToLogin(request, response);
        }else{
            sendNotPermitted(request, response);
        }
    }

    /**
     * 跳转到登录界面
     * @param request
     * @param response
     */
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     *
     * @param request
     * @param response
     */
    private void sendNotPermitted(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * 是否访问项目根路径
     * @param request
     * @return
     */
    protected boolean isRootPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return StringUtils.isEmpty(pathInfo) || "/".equals(pathInfo);
    }
}
