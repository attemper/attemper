package com.thor.sys.ext.service;

import com.thor.sys.ext.annotation.SecretStrategyType;
import com.thor.sys.ext.secret.SecretStrategy;
import com.thor.sys.ext.secret.md5.MD5Strategy;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author ldang
 */
@Service
@Slf4j
public class SecretService {

    @Value("${secret.strategy:0}")
    private int strategy;

    private SecretStrategy secretStrategy;

    @PostConstruct
    private void initStrategy(){
        Reflections reflections = new Reflections(SecretStrategy.class.getPackage().getName());
        Set<Class<? extends SecretStrategy>> subTypeSet = reflections.getSubTypesOf(SecretStrategy.class);
        try {
            for (Class<? extends SecretStrategy> subType : subTypeSet) {
                SecretStrategyType annotation = subType.getAnnotation(SecretStrategyType.class);
                if (annotation.value() == strategy) {
                    secretStrategy = subType.newInstance();
                    break;
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            secretStrategy = new MD5Strategy();  //默认选择md5加密
        }
    }

    /**
     * 加密
     * @param origin
     * @return
     */
    public String encode(String origin) {
        return secretStrategy.encode(origin);
    }
}
