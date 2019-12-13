package com.github.attemper.config.base.aspect;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.config.base.annotation.MultiDataSource;
import com.github.attemper.config.base.datasource.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * multi datasource exchange aspect
 */
@Aspect
@Component
public class MultiDataSourceAspect implements Ordered {

    @Pointcut("@annotation(" + GlobalConstants.basePackageLocation + "config.base.annotation.MultiDataSource)")
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
