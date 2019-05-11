package com.github.attemper.security.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.entity.ApiLog;
import com.github.attemper.config.base.service.ApiLogService;
import com.github.attemper.config.base.util.IPUtil;
import com.github.attemper.config.base.util.ServletUtil;
import com.github.attemper.security.model.JWTToken;
import com.github.attemper.sys.exception.JWTDecodedException;
import com.github.attemper.sys.exception.JWTExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * json web token filter of shiro
 * @author ldang
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * step1:校验token是否存在
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        if ((WebUtils.toHttp(request).getHeader(CommonConstants.token) == null)) {
            return printError(response, 1002);
        }
        return super.preHandle(request, response);
    }

    /**
     * setp2.1返回false，以执行onAccessDenied
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * step2.2:存在token时，允许登录
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        try{
            executeLogin(request, response);
        }catch (JWTDecodedException e){
            //log.error(e.getMessage(), e);
            return printError(response, 1001);
        }catch (JWTExpiredException e){
            //log.error(e.getMessage(), e);
            return printError(response, 1000);
        }
        return true;
    }

    /**
     * step3:执行登录方法
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String accessToken = WebUtils.toHttp(request).getHeader(CommonConstants.token);
        JWTToken token = new JWTToken(accessToken);
        SecurityUtils.getSubject().login(token);  //会请求ream中的认证方法
        return true;
    }

    /**
     * 打印异常并返回false
     * @param response
     * @param code
     * @return
     */
    private boolean printError(ServletResponse response, int code) {
        try {
            ((HttpServletResponse) response).setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            CommonResult result = CommonResult.put(code);
            String json = SpringContextAware.getBean(ObjectMapper.class).writeValueAsString(result);
            response.getWriter().print(json);
            saveLog(result);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 保存报错日志
     * @param result
     */
    private void saveLog(CommonResult result) {
        ApiLog apiLog = ApiLog.builder()
                .code(result.getCode())
                .msg(result.getMsg())
                .duration(result.getDuration())
                .responseTime(result.getResponseTime())
                .className(this.getClass().getName())
                .param(ServletUtil.getHeader(CommonConstants.token))
                .ip(IPUtil.getIpAddr())
                .build();
        SpringContextAware.getBean(ApiLogService.class).save(apiLog);
    }
}
