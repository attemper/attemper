package com.sse.attemper.web.aspect;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.constant.GlobalConstants;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.CommonParam;
import com.sse.attemper.common.param.sys.tenant.TenantGetParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.config.annotation.IgnoreLogResult;
import com.sse.attemper.config.entity.ApiLog;
import com.sse.attemper.config.service.ApiLogService;
import com.sse.attemper.config.util.IPUtil;
import com.sse.attemper.config.util.ServletUtil;
import com.sse.attemper.config.util.StringUtil;
import com.sse.attemper.security.ext.service.JWTService;
import com.sse.attemper.sys.holder.TenantHolder;
import com.sse.attemper.sys.holder.UserHolder;
import com.sse.attemper.sys.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

/**
 * @auth ldang
 * Controller统一处理
 */
@Slf4j
@Component
@Aspect
public class ControllerAspect {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApiLogService apiLogService;

    @Autowired
    private TenantService tenantService;

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 切点<br>
     */
    @Pointcut("(execution(public * " + GlobalConstants.basePackageLocation + "sys.controller.*Controller.*(..)))"
            + "||"
            + "(execution(public * " + GlobalConstants.basePackageLocation + "core.controller..*.*Controller.*(..)))"
            + "||"
            + "(execution(public * " + GlobalConstants.basePackageLocation + "security.controller.*Controller.*(..)))"
    )
    public void aroundController() {

    }

    /**
     * 环切
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Around("aroundController()")
    public CommonResult around(ProceedingJoinPoint joinPoint) throws Throwable {
        ApiLog apiLog = new ApiLog();
        CommonResult result = checkParam(joinPoint, apiLog);
        if (result != null) {
            return result;
        }
        return executeMethod(joinPoint, apiLog);
    }

    /**
     * 执行service
     *
     * @param joinPoint
     * @param apiLog
     * @return
     * @throws Throwable
     */
    private CommonResult executeMethod(ProceedingJoinPoint joinPoint, ApiLog apiLog) throws Throwable {
        CommonResult result = null;
        // 校验通过
        Instant begin = Instant.now();
        try {
            result = (CommonResult) joinPoint.proceed();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            IgnoreLogResult ignoreLogResult = method.getAnnotation(IgnoreLogResult.class);
            if (ignoreLogResult == null || !ignoreLogResult.value() && result != null) { // 不忽略结果
                apiLog.setResult(result.toString());
            }
            return result;
        } catch (RTException rte) {
            result = CommonResult.error(rte);
            throw rte;
        } catch (Exception e){
            result = CommonResult.error(e.getMessage());
            throw e;
        }finally {
            if (result != null) {
                Instant end = Instant.now();
                result.setDuration(String.valueOf(Duration.between(begin, end).toMillis() / 1000.0));// 请求耗时
            }
            UserHolder.clear();
            TenantHolder.clear();
            apiLogService.save(resultToLog(result, apiLog));
        }
    }

    /**
     * 校验参数
     *
     * @param joinPoint
     * @param apiLog
     * @return
     */
    private CommonResult checkParam(ProceedingJoinPoint joinPoint, ApiLog apiLog) {
        CommonResult result = null;
        // 处理日志的分类，方法等
        preHandleLog(joinPoint, apiLog);
        String tenantId = ServletUtil.getHeader(CommonConstants.tenantId);
        apiLog.setTenantId(tenantId);
        Tenant tenant = tenantService.get(new TenantGetParam(tenantId));
        if (tenant == null) {
            return CommonResult.putAdd(1500, tenantId);
        }
        String sign = ServletUtil.getHeader(CommonConstants.sign);
        if (!StringUtils.equals(sign, tenant.getSign())) {
            return CommonResult.putAdd(1501, sign);
        }
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (arg != null) {
                    //1.validator校验
                    Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                    if (!violations.isEmpty()) {
                        ConstraintViolation<Object> violation = violations.iterator().next();
                        String codeStr = violation.getMessage();
                        return CommonResult.put(Integer.valueOf(codeStr));
                    }
                    if (arg instanceof CommonParam) {
                        CommonParam commonParam = (CommonParam) arg;
                        commonParam.preHandle();  //预处理参数
                        //2.补充校验
                        String codeStr = commonParam.validate();
                        if (codeStr != null) {
                            try {
                                int code = Integer.parseInt(codeStr);
                                return CommonResult.put(code);
                            } catch (NumberFormatException e) {
                                return CommonResult.error(codeStr);
                            }
                        }
                        if (apiLog.getUserName() == null) {
                            apiLog.setUserName(resolveUserNameFromParam(commonParam));
                        }
                    }
                }
            }
        }
        return result;
    }

    private void preHandleLog(ProceedingJoinPoint joinPoint, ApiLog apiLog) {
        apiLog.setIp(IPUtil.getIpAddr());
        apiLog.setUserName(resolveUserNameFromToken());
        apiLog.setParam(StringUtil.formatToJsonStr(joinPoint.getArgs()));

        Class<?> clazz = joinPoint.getTarget().getClass();
        Api api = clazz.getAnnotation(Api.class);
        if (api != null && api.tags() != null && api.tags().length > 0) {
            apiLog.setTag(api.tags()[0]); // 记录类标签
        }
        apiLog.setClassName(clazz.getName()); // 记录类名称

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            apiLog.setOperation(apiOperation.value()); // 记录类标签
        }
        apiLog.setMethod(method.getName()); // 记录方法名称
        resolvePath(method, apiLog);
    }

    private void resolvePath(Method method, ApiLog apiLog) {
        Annotation[] annotations = method.getAnnotations();
        if(annotations != null){
            for (Annotation annotation : annotations) {
                if(annotation instanceof RequestMapping){
                    RequestMapping mappingAnnotation = (RequestMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof GetMapping) {
                    GetMapping mappingAnnotation = (GetMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof PostMapping) {
                    PostMapping mappingAnnotation = (PostMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof PutMapping) {
                    PutMapping mappingAnnotation = (PutMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof DeleteMapping) {
                    DeleteMapping mappingAnnotation = (DeleteMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                }
            }
        }
    }

    /**
     * 从token中解析用户信息
     * @return
     */
    private String resolveUserNameFromToken(){
        String token = ServletUtil.getHeader(CommonConstants.token);
        if (StringUtils.isNotBlank(token) && !CommonConstants._null.equals(token)
                && !CommonConstants.undefined.equals(token) && !CommonConstants._false.equals(token)) {
            User user = null;
            try {
                user = jwtService.parseTokenToUser(token);
            } catch (Exception e) { // 很可能该接口未被shiro拦截，应当考虑白名单的设置
                log.error(e.getMessage(), e);
            }
            if(user != null){
                return user.getUserName();
            }
        }
        return null;
    }

    /**
     * 从参数中解析用户信息
     * @param commonParam
     * @return
     */
    private String resolveUserNameFromParam(CommonParam commonParam) {
        try{
            Field field = commonParam.getClass().getDeclaredField(CommonConstants.userName);
            field.setAccessible(true);
            return (String) field.get(commonParam);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将BaseResult中的数据设置给log
     * @param result
     * @param apiLog
     */
    private static ApiLog resultToLog(CommonResult result, ApiLog apiLog) {
        if(result != null){
            apiLog.setCode(result.getCode());
            apiLog.setMsg(result.getMsg());
            apiLog.setResponseTime(result.getResponseTime());
            apiLog.setDuration(result.getDuration());
        }
        return apiLog;
    }
}
