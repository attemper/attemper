package com.github.attemper.sys.exception;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.authc.AuthenticationException;


/**
 * jwt decode exception
 */
public class JWTDecodedException extends AuthenticationException {

    public JWTDecodedException (MalformedJwtException e){
        super(e);
    }

    public JWTDecodedException (SignatureException e){
        super(e);
    }

}
