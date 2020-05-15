package com.github.attemper.sys.aspect;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.annotation.IgnoreLogResult;
import com.github.attemper.config.base.entity.ApiLog;
import com.github.attemper.config.base.util.AspectUtil;
import com.github.attemper.config.base.util.IPUtil;
import com.github.attemper.config.base.util.ServletUtil;
import com.github.attemper.config.base.util.StringUtil;
import com.github.attemper.java.sdk.common.param.BaseParam;
import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.sys.ext.jwt.JWTService;
import com.github.attemper.sys.holder.TenantHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Slf4j
@Component
@Aspect
public class ControllerAspect {

    @Autowired
    private JWTService jwtService;

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    @Pointcut("(execution(public * " + GlobalConstants.basePackageLocation + "*.controller..*Controller.*(..)))"
    )
    public void aroundController() {

    }

    @Around("aroundController()")
    public CommonResult around(ProceedingJoinPoint joinPoint) throws Throwable {
        ApiLog apiLog = new ApiLog();
        CommonResult result = checkParam(joinPoint, apiLog);
        if (result != null) {
            return result;
        }
        return executeMethod(joinPoint, apiLog);
    }

    private CommonResult executeMethod(ProceedingJoinPoint joinPoint, ApiLog apiLog) throws Throwable {
        CommonResult result = null;
        Instant begin = Instant.now();
        try {
            result = (CommonResult) joinPoint.proceed();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            IgnoreLogResult ignoreLogResult = method.getAnnotation(IgnoreLogResult.class);
            if (result != null && result.getResult() != null && (ignoreLogResult == null || !ignoreLogResult.value())) {
                apiLog.setResult(result.getResult().toString());
            }
            return result;
        } catch (RTException rte) {
            result = CommonResult.error(rte);
            throw rte;
        } catch (Exception e){
            result = CommonResult.error(e.getMessage());
            log.error(e.getMessage(), e);
            throw e;
        }finally {
            if (result != null) {
                Instant end = Instant.now();
                result.setDuration(String.valueOf(Duration.between(begin, end).toMillis() / 1000.0));
            }
            TenantHolder.clear();
            AspectUtil.saveLog(apiLog, result);
        }
    }

    private CommonResult checkParam(ProceedingJoinPoint joinPoint, ApiLog apiLog) {
        CommonResult result = null;
        preHandleLog(joinPoint, apiLog);
        Object[] args = joinPoint.getArgs();
        boolean isLogin = false;
        if (args != null) {
            for (Object arg : args) {
                if (arg != null) {
                    Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                    if (!violations.isEmpty()) {
                        ConstraintViolation<Object> violation = violations.iterator().next();
                        String codeStr = violation.getMessage();
                        return CommonResult.put(Integer.valueOf(codeStr));
                    }
                    if (arg instanceof BaseParam) {
                        BaseParam baseParam = (BaseParam) arg;
                        if (arg instanceof CommonParam) {
                            ((CommonParam) baseParam).preHandle();
                        }
                        String codeStr = baseParam.validate();
                        if (codeStr != null) {
                            try {
                                int code = Integer.parseInt(codeStr);
                                return CommonResult.put(code);
                            } catch (NumberFormatException e) {
                                return CommonResult.error(codeStr);
                            }
                        }
                        if (!isLogin && arg instanceof LoginParam) {
                            isLogin = true;
                        }
                    }
                }
            }
        }
        Tenant tenant = TenantHolder.get();
        if (tenant == null && !isLogin) {
            String token = ServletUtil.getHeader(CommonConstants.token);
            if (StringUtils.isNotBlank(token)) {
                tenant = jwtService.parseToken(token);
                TenantHolder.set(tenant);
            }
        }
        if (tenant != null) {
            apiLog.setTenantId(tenant.getUserName());
        }
        return result;
    }

    private void preHandleLog(ProceedingJoinPoint joinPoint, ApiLog apiLog) {
        apiLog.setIp(IPUtil.getIpAddr());
        apiLog.setParam(StringUtil.formatToJsonStr(joinPoint.getArgs()));

        Class<?> clazz = joinPoint.getTarget().getClass();
        Tag api = clazz.getAnnotation(Tag.class);
        if (api != null) {
            apiLog.setTag(api.name());
        }
        apiLog.setClassName(clazz.getName());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Operation apiOperation = method.getAnnotation(Operation.class);
        if (apiOperation != null) {
            apiLog.setOperation(apiOperation.description());
        }
        apiLog.setMethod(method.getName());
        AspectUtil.resolvePath(method, apiLog);
    }
}
