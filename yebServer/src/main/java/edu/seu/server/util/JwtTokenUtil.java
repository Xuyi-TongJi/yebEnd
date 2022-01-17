package edu.seu.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt工具类
 * @author xuyitjuseu
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private String secret;
    @Getter
    private String tokenHeader;
    private Long expiration;
    @Getter
    private String tokenHead;

    /**
     * 生成token
     * @param userDetails 用户信息
     * @return token字符串
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generationTokenByClaims(claims);
    }

    /**
     * 根据token中的负载获取用户名
     * @param token token字符串
     * @return 用户名字符串
     */
    public String getUsernameByToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 判断token是否失效，如果token失效或者token中的用户名与UserDetails中的用户名不一致，那么token失效
     * @param token token字符串
     * @param userDetails user权限对象（当前授权并登录的用户）
     * @return token是否失效的布尔值
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        if (!getUsernameByToken(token).equals(userDetails.getUsername())) {
            return false;
        } else {
            return !isTokenExpired(token);
        }
    }

    /**
     * 判断token是否能被刷新，即：如果token超时，那么token可以被刷新，如果能被刷新，则刷新token
     * @param token token字符串
     * @return 如果能被刷新，则返回新的token，如果不能被刷新，则返回原token
     */
    public String refreshToken(String token) {
        if (!isTokenExpired(token)) {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generationTokenByClaims(claims);
        }
        return token;
    }

    /**
     * 根据claims构建token的似有辅助方法
     * @param claims 荷载
     * @return token字符串
     */
    private String generationTokenByClaims(Map<String, Object> claims) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setExpiration(setTokenExpiration())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 根据expiration设置token的失效时间
     * @return 失效时间
     */
    private Date setTokenExpiration() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    /**
     * 根据token获取负载
     * @param token token字符串
     * @return 负载payload字符串
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断token是否已经过期
     * @param token token字符串
     * @return 是否过期的布尔值
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getClaimsFromToken(token).getExpiration();
        return expiredDate.before(new Date());
    }
}