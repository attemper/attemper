package com.sse.attemper.security.ext.jwt.javajwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.security.exception.JWTDecodedException;
import com.sse.attemper.security.exception.JWTExpiredException;
import com.sse.attemper.security.ext.annotation.JWTStrategyType;
import com.sse.attemper.security.ext.jwt.JWTStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * java-jwt实现json web token(jwt)的签名和验证
 * @author ldang
 */
@JWTStrategyType(0)
@Slf4j
public class JavaJWTStrategy implements JWTStrategy {

    @Override
    public String createToken(long expireMills, User user) {
        long currMills = System.currentTimeMillis();
        Date now = new Date(currMills);
        String userJsonStr;
        try {
            userJsonStr = ContextBeanAware.getBean(ObjectMapper.class).writeValueAsString(user);
        } catch (JsonProcessingException e){
            log.error(e.getMessage(), e);
            userJsonStr = "";
        }
        return JWT.create()
                .withIssuer(user.getUserName())
                .withJWTId(UUID.randomUUID().toString())
                .withClaim(USER, userJsonStr)
                .withIssuedAt(now)
                .withExpiresAt(new Date(currMills + expireMills))
                .sign(Algorithm.HMAC256(SECRET));
    }

    @Override
    public User parseTokenToUser(String token) throws JWTExpiredException, JWTDecodedException {
        try{
            DecodedJWT decodeJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            String userJsonStr = Optional.of(decodeJWT.getClaim(USER)).get().asString();
            return ContextBeanAware.getBean(ObjectMapper.class).readValue(userJsonStr, User.class);
        }catch (JWTDecodeException e){
            throw new JWTDecodedException(e);
        }catch (SignatureVerificationException e){
            throw new JWTDecodedException(e);
        }catch (TokenExpiredException e){
            throw new JWTExpiredException(e);
        }catch(Exception e){
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, e);
        }
    }
}
