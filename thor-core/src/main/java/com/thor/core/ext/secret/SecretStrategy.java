package com.thor.core.ext.secret;

/**
 * @author ldang
 */
public interface SecretStrategy {

    String encode(String origin);
}
