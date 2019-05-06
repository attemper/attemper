package com.github.attemper.security.ext.service;

import com.github.attemper.security.exception.JWTDecodedException;
import com.github.attemper.security.exception.JWTExpiredException;
import com.github.attemper.security.ext.annotation.JWTStrategyType;
import com.github.attemper.security.ext.jwt.JWTStrategy;
import com.github.attemper.security.ext.jwt.jjwt.JJWTStrategy;
import com.github.attemper.common.result.sys.user.User;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

@Slf4j
@Service
public class JWTService {

    @Value("${jwt.strategy:0}")
    private int strategy;

    @Value("${jwt.expire-duration:7200000}")
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
            jwtStrategy = new JJWTStrategy();
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
