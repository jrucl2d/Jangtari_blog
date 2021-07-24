package com.yu.jangtari.util;

import com.yu.jangtari.domain.RoleType;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    private final int ACCESS_TOKEN_VALID_TIME;
    private final int REFRESH_TOKEN_VALID_TIME;
    private final String JWT_SECRET_KEY;

    public JWTUtil(JwtAndCookieInfo jwtAndCookieInfo) {
        this.ACCESS_TOKEN_VALID_TIME = jwtAndCookieInfo.getAccessTokenValidTime();
        this.REFRESH_TOKEN_VALID_TIME = jwtAndCookieInfo.getRefreshTokenValidTime();
        this.JWT_SECRET_KEY = Base64.getEncoder().encodeToString( jwtAndCookieInfo.getJwtSecretKey().getBytes());
    }

    // JWT 토큰 생성 관련 메소드
    public String createAccessToken(String username, RoleType roleType) {
        return createToken(username, roleType, ACCESS_TOKEN_VALID_TIME);
    }
    public String createRefreshToken(String username, RoleType roleType) {
        return createToken(username, roleType, REFRESH_TOKEN_VALID_TIME);
    }
    private String createToken(final String username, final RoleType role, long expireTime) {
        final Date now = new Date();
        final Claims claims = Jwts.claims(); // JWT payload에 저장되는 정보 claim
        claims.put("username", username); // key-value 쌍으로 저장됨
        claims.put("role", role.name());

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 발행 시간
                .setExpiration(new Date(now.getTime() + expireTime)) // 유효 기간
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY) // 암호화 알고리즘
                .compact();
    }

    // JWT 토큰 parsing 관련 메소드
    public String getUsernameFromJWT(final String token) {
        return getClaims(token).getBody().get("username", String.class);
    }
    public RoleType getRoleFromJWT(final String token) {
        final String role = getClaims(token).getBody().get("role", String.class);
        return RoleType.of(role);
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);
    }

    // 토큰의 유효성, 만료일자 확인 -> getClaims 메소드의 parseClaimsJws(token)에서 validation checking이 이루어짐
    public void validateToken(final String token) throws ExpiredJwtException,
        UnsupportedJwtException,
        MalformedJwtException,
        SignatureException,
        IllegalArgumentException
    {
        getClaims(token);
    }
}
