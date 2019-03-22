package com.sse.attemper.sys.ext.secret.md5;

import com.sse.attemper.sys.ext.annotation.SecretStrategyType;
import com.sse.attemper.sys.ext.secret.SecretStrategy;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;

/**
 * @author ldang
 */
@SecretStrategyType(0)
public class MD5Strategy implements SecretStrategy {

    @Override
    public String encode(String origin) {
        return DigestUtil.md5Hex(origin);
    }
}
