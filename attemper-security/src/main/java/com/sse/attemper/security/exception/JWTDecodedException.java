package com.sse.attemper.security.exception;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.authc.AuthenticationException;


/**
 * jwt解码异常
 * @author ldang
 */
public class JWTDecodedException extends AuthenticationException {

    public JWTDecodedException (MalformedJwtException e){
        super(e);
    }

    public JWTDecodedException (SignatureException e){
        super(e);
    }

}
