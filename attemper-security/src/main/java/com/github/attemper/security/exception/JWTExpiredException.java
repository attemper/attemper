package com.github.attemper.security.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * @author ldang
 */
public class JWTExpiredException extends AuthenticationException {

    public JWTExpiredException (ExpiredJwtException e){
        super(e);
    }
}
