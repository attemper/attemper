package com.thor.config.aspect;

import com.thor.common.constant.GlobalConstants;
import com.thor.config.annotation.MultiDataSource;
import com.thor.config.datasource.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 * 
 * @auth ldang
 */
@Aspect
@Component
public class MultiDataSourceAspect implements Ordered {
    
	@Pointcut("@annotation(" + GlobalConstants.basePackageLocation + "config.annotation.MultiDataSource)")
    public void dataSourcePointcut() {

    }

    @Around("dataSourcePointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        MultiDataSource ds = method.getAnnotation(MultiDataSource.class);
        if(ds != null){
        	DynamicDataSource.setDataSource(ds.value());
        }
        try {
            return point.proceed();
        } finally {
        	DynamicDataSource.clearDataSource();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
