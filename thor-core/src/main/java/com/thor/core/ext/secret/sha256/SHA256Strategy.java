package com.thor.core.ext.secret.sha256;

import com.thor.core.ext.annotation.SecretStrategyType;
import com.thor.core.ext.secret.SecretStrategy;
import com.thor.core.ext.annotation.SecretStrategyType;
import com.thor.core.ext.secret.SecretStrategy;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;

/**
 * @author ldang
 */
@SecretStrategyType(1)
public class SHA256Strategy implements SecretStrategy {

    @Override
    public String encode(String origin) {
        return DigestUtil.sha256Hex(origin);
    }
}
