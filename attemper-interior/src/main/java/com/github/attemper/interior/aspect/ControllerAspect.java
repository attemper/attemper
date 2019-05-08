package com.github.attemper.interior.aspect;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.config.base.entity.ApiLog;
import com.github.attemper.config.base.util.AspectUtil;
import com.github.attemper.config.base.util.IPUtil;
import com.github.attemper.config.base.util.ServletUtil;
import com.github.attemper.config.base.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

/**
 * AOP of Controller
 */
@Slf4j
//@Component
//@Aspect
public class ControllerAspect {

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    //@Pointcut("(execution(public * " + GlobalConstants.basePackageLocation + "interior.controller.*Controller.*(..)))")
    public void aroundController() {}

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
     * @param joinPoint
     * @param apiLog
     * @return
     * @throws Throwable
     */
    private CommonResult executeMethod(ProceedingJoinPoint joinPoint, ApiLog apiLog) throws Throwable {
        CommonResult result = null;
        Instant begin = Instant.now();
        try {
            result = (CommonResult) joinPoint.proceed();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            if (result.getResult() != null) {
                apiLog.setResult(result.getResult().toString());
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
            AspectUtil.saveLog(apiLog, result);
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
        preHandleLog(joinPoint, apiLog);
        String tenantId = ServletUtil.getHeader(CommonConstants.tenantId);
        apiLog.setTenantId(tenantId);
        /*Tenant adminTenant = tenantService.get(new TenantGetParam(tenantId));
        if (adminTenant == null) {
            return CommonResult.putAdd(1500, tenantId);
        }
        String sign = ServletUtil.getHeader(CommonConstants.sign);
        if (!StringUtils.equals(sign, adminTenant.getSign())) {
            return CommonResult.putAdd(1501, sign);
        }
        TenantHolder.set(adminTenant);
        String userNameOfAdmin = adminTenant.getAdmin();
        apiLog.setUserName(userNameOfAdmin);*/
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
                    }
                }
            }
        }
        return result;
    }

    private void preHandleLog(ProceedingJoinPoint joinPoint, ApiLog apiLog) {
        apiLog.setIp(IPUtil.getIpAddr());
        apiLog.setParam(StringUtil.formatToJsonStr(joinPoint.getArgs()));

        Class<?> clazz = joinPoint.getTarget().getClass();
        Api api = clazz.getAnnotation(Api.class);
        if (api != null) {
            apiLog.setTag(api.value());
        }
        apiLog.setClassName(clazz.getName());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            apiLog.setOperation(apiOperation.value());
        }
        apiLog.setMethod(method.getName());
        AspectUtil.resolvePath(method, apiLog);
    }

}
