package com.thor.security.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.authc.AuthenticationException;


/**
 * jwt解码异常
 * @author ldang
 */
public class JWTDecodedException extends AuthenticationException {

    public JWTDecodedException (JWTDecodeException e){
        super(e);
    }

    public JWTDecodedException (SignatureVerificationException e){
        super(e);
    }

    public JWTDecodedException (MalformedJwtException e){
        super(e);
    }

    public JWTDecodedException (SignatureException e){
        super(e);
    }

}
