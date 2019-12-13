package com.github.attemper.sys.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * jwt expire exception
 */
public class JWTExpiredException extends AuthenticationException {

    public JWTExpiredException (ExpiredJwtException e){
        super(e);
    }
}
