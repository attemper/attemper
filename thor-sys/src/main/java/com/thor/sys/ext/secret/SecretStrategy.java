package com.thor.sys.ext.secret;

/**
 * @author ldang
 */
public interface SecretStrategy {

    String encode(String origin);
}
