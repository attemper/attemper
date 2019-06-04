package com.github.attemper.config.base.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * get spring context
 * @author ldang
 */
@Component
public class SpringContextAware implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * get bean by T.class
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> beanClass){
        return context.getBean(beanClass);
    }

    /**
     * get bean by name
     *
     * @param name bean name
     * @return bean of object
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * get bean by name and T.class
     *
     * @param beanClass
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> beanClass, String name) {
        Object bean = context.getBean(name);
        return beanClass.isInstance(bean) ? (T) bean : null;
    }
}
