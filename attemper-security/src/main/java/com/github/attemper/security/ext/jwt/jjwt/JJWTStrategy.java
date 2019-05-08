package com.github.attemper.security.ext.jwt.jjwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.sys.user.User;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.security.exception.JWTDecodedException;
import com.github.attemper.security.exception.JWTExpiredException;
import com.github.attemper.security.ext.annotation.JWTStrategyType;
import com.github.attemper.security.ext.jwt.JWTStrategy;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ldang
 * JJWT实现json web token(jwt)的签名和验证
 */
@JWTStrategyType(0)
@Slf4j
public class JJWTStrategy implements JWTStrategy {

    @Override
    public String createToken(long expireMills, User user) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long currMillis = System.currentTimeMillis();
        Date now = new Date(currMillis);

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        String userJsonStr;
        try {
            userJsonStr = SpringContextAware.getBean(ObjectMapper.class).writeValueAsString(user);
        } catch (JsonProcessingException e){
            log.error(e.getMessage(), e);
            userJsonStr = "";
        }
        claims.put(USER, userJsonStr);

        //下面就是在为payload添加各种标准声明和私有声明
        return Jwts.builder()
                .signWith(signatureAlgorithm, SECRET)  //设置签名使用的签名算法和签名使用的秘钥
                .setClaims(claims)  //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(UUID.randomUUID().toString())  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)  //iat: jwt的签发时间
                .setSubject(user.getUserName())  //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setExpiration(new Date(currMillis + expireMills))//设置过期时间
                .compact();
    }

    @Override
    public User parseTokenToUser(String token) throws JWTExpiredException, JWTDecodedException {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            String userJsonStr = claims.get(USER, String.class);
            return SpringContextAware.getBean(ObjectMapper.class).readValue(userJsonStr, User.class);
        }catch (MalformedJwtException e){
            throw new JWTDecodedException(e);
        }catch (SignatureException e){
            throw new JWTDecodedException(e);
        }catch (ExpiredJwtException e){
            throw new JWTExpiredException(e);
        }catch(Exception e){
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, e);
        }

    }
}
