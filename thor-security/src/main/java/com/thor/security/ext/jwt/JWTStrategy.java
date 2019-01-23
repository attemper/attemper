package com.thor.security.ext.jwt;

import com.thor.sdk.common.result.user.User;
import com.thor.security.exception.JWTDecodedException;
import com.thor.security.exception.JWTExpiredException;
import org.apache.commons.lang.StringUtils;

public interface JWTStrategy {
    String SECRET = "1!2@3#4$5%6^7&8*";

    String USER = "user";

    /**
     * 根据过期时间和用户对象生成json web token
     * @param expireMills
     * @param user
     * @return
     */
    String createToken(long expireMills, User user);

    /**
     * 从json web token中取出用户名和密码
     * @param token
     * @return
     */
    User parseTokenToUser(String token) throws JWTExpiredException, JWTDecodedException;

    /**
     * 验证token与user是否一致
     * @param token
     * @param user
     * @return
     */
    default boolean verify(String token, User user){
        User tokenUser = parseTokenToUser(token);
        return StringUtils.equalsIgnoreCase(user.getUserName(), tokenUser.getUserName())
                && StringUtils.equals(user.getPassword(), tokenUser.getPassword())
                && StringUtils.equals(user.getTenantId(), user.getTenantId());
    }
}
