package com.thor.security.ext.service;

import com.thor.sdk.common.result.sys.user.User;
import com.thor.security.exception.JWTDecodedException;
import com.thor.security.exception.JWTExpiredException;
import com.thor.security.ext.annotation.JWTStrategyType;
import com.thor.security.ext.jwt.JWTStrategy;
import com.thor.security.ext.jwt.javajwt.JavaJWTStrategy;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

@Slf4j
@Service
public class JWTService {

    @Value("${jwt.strategy:1}")
    private int strategy;

    @Value("${jwt.expire-duration:3600000}")
    private long expireDuration;

    private JWTStrategy jwtStrategy;

    @PostConstruct
    private void initStrategy(){
        Reflections reflections = new Reflections(JWTStrategy.class.getPackage().getName());
        Set<Class<? extends JWTStrategy>> subTypeSet = reflections.getSubTypesOf(JWTStrategy.class);
        try {
            for (Class<? extends JWTStrategy> subType : subTypeSet) {
                JWTStrategyType jwtAnnotation = subType.getAnnotation(JWTStrategyType.class);
                if (jwtAnnotation.value() == strategy) {
                    jwtStrategy = subType.newInstance();
                    break;
                }
            }
        } catch (InstantiationException|IllegalAccessException e) {
            log.error(e.getMessage(), e);
            jwtStrategy = new JavaJWTStrategy();  //默认选择java-jwt实现
        }
    }

    public String createToken(User user){
        return jwtStrategy.createToken(expireDuration, user);
    }

    public User parseTokenToUser(String token) throws JWTExpiredException, JWTDecodedException {
        return jwtStrategy.parseTokenToUser(token);
    }

    public boolean verify(String token, User user){
        return jwtStrategy.verify(token, user);
    }
}
