package com.thor.security.model;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ldang
 */
public class JWTToken implements AuthenticationToken {

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return getCredentials();
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
