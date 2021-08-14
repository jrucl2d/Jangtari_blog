package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String USERNAME_KEY = "username";
    private static final String ROLE_KEY = "role";
    private static final String REFRESH_KEY = "refresh";
    private static final String JWT_SECRET = "secret";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 2 * 60L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60L;

    public String createAccessToken(JwtInfo jwtInfo) {
        return createAccessToken(jwtInfo, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createAccessToken(JwtInfo jwtInfo, long expireTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME_KEY, jwtInfo.getUsername());
        claims.put(ROLE_KEY, jwtInfo.getRoleType().name());
        return createToken(claims, expireTime);
    }

    public String createRefreshToken(String token) {
        return createRefreshToken(token, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(String token, long expireTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(REFRESH_KEY, token);
        return createToken(claims, expireTime);
    }

    private String createToken(Map<String, Object> claims, long expireTime) {
        return Jwts.builder()
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 발행 시간
            .addClaims(claims)
            .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(expireTime))) // 유효 기간
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 암호화 알고리즘
            .compact();
    }

    // JWT 토큰 parsing 관련 메소드
    public JwtInfo parseAccessToken(String token) {
        if (token == null) throw new IllegalArgumentException();
        Claims claims = getClaims(token);
        String username = claims.get(USERNAME_KEY, String.class);
        String role = claims.get(ROLE_KEY, String.class);
        return new JwtInfo(username, RoleType.of(role));
    }
    public String parseRefreshToken(String token) {
        if (token == null) throw new IllegalArgumentException();
        Claims claims = getClaims(token);
        return claims.get(REFRESH_KEY, String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }
}
