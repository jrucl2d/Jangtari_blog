package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.experimental.UtilityClass;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@UtilityClass
public class JwtUtil
{
    private final String USERNAME_KEY = "username";
    private final String ROLE_KEY = "role";
    private final String JWT_SECRET = "tmp";
    private final Key tokenKey = new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

    public String createToken(JwtInfo jwtInfo, long expireTime) {
        return Jwts.builder()
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 발행 시간
                .claim(USERNAME_KEY, jwtInfo.getUsername())
                .claim(ROLE_KEY, jwtInfo.getRoleType().name())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(expireTime))) // 유효 기간
                .signWith(SignatureAlgorithm.HS256, tokenKey) // 암호화 알고리즘
                .compact();
    }

    // JWT 토큰 parsing 관련 메소드
    public JwtInfo parseJwt(String token) throws ExpiredJwtException,
        UnsupportedJwtException,
        MalformedJwtException,
        SignatureException,
        IllegalArgumentException
    {
        Claims claims = getClaims(token);
        String username = claims.get(USERNAME_KEY, String.class);
        String role = claims.get(ROLE_KEY, String.class);
        return new JwtInfo(username, RoleType.of(role));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token).getBody();
    }

}
