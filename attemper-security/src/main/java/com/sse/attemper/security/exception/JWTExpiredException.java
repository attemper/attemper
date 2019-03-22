package com.sse.attemper.security.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * jwt过期异常
 * @author ldang
 */
public class JWTExpiredException extends AuthenticationException {

    public JWTExpiredException (TokenExpiredException e){
        super(e);
    }

    public JWTExpiredException (ExpiredJwtException e){
        super(e);
    }
}
