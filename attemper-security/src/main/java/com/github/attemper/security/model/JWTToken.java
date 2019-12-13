package com.github.attemper.security.model;

import org.apache.shiro.authc.AuthenticationToken;

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
