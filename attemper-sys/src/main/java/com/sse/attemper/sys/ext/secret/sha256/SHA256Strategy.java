package com.sse.attemper.sys.ext.secret.sha256;

import com.sse.attemper.sys.ext.annotation.SecretStrategyType;
import com.sse.attemper.sys.ext.secret.SecretStrategy;
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
