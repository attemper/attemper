package com.github.attemper.interior.service;

import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.java.sdk.common.executor2biz.param.BeanParam;
import com.github.attemper.java.sdk.common.executor2biz.param.RouterParam;
import com.github.attemper.java.sdk.common.executor2biz.param.TaskExecutionParam;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Service
public class RouterService {

    public Object route(RouterParam routerParam) {
        BeanParam beanParam = routerParam.getBeanParam();
        Object bean = SpringContextAware.getBean(beanParam.getBeanName());
        Method method = ReflectionUtils.findMethod(bean.getClass(), beanParam.getMethodName(), TaskExecutionParam.class);
        TaskExecutionParam taskExecutionParam = new TaskExecutionParam();
        taskExecutionParam.setExecutionParam(routerParam.getExecutionParam())
                .setBizParam(BeanUtil.map2Bean(antiProxy(bean, method), routerParam.getBizParamMap()));
        return ReflectionUtils.invokeMethod(method, bean, taskExecutionParam);
    }

    /**
     * get actual method anti proxy
     * @param bean
     * @param method
     * @return
     */
    private Class<?> antiProxy(Object bean, Method method) {
        Method mostSpecificMethod = AopUtils.getMostSpecificMethod(method, AopUtils.getTargetClass(bean));
        ResolvableType resolvableType = ResolvableType.forMethodParameter(mostSpecificMethod, 0);
        return resolvableType.getGeneric(0).resolve();
    }
}
