package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil
{
    private static final String USERNAME_KEY = "username";
    private static final String ROLE_KEY = "role";
    private static final int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000; // Access token 2분
    private static final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일
    private static final String JWT_SECRET = "tmp";
    private final Key tokenKey;

    public JwtUtil() {
        this.tokenKey = new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    // JWT 토큰 생성 관련 메소드
    public String createAccessToken(JwtInfo jwtInfo) {
        return createToken(jwtInfo, ACCESS_TOKEN_VALID_TIME);
    }
    public String createRefreshToken(JwtInfo jwtInfo) {
        return createToken(jwtInfo, REFRESH_TOKEN_VALID_TIME);
    }

    // accessToken이 만료되었을 경우 refreshToken으로 accessToken을 재생성
    public String recreateAccessToken(String refreshToken) {
        JwtInfo jwtInfo = parseJwt(refreshToken);
        return createAccessToken(jwtInfo);
    }
    public String createToken(JwtInfo jwtInfo, long expireTime) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME_KEY, jwtInfo.getUsername());
        claims.put(ROLE_KEY, jwtInfo.getRoleType().name());
        return Jwts.builder()
                .setIssuedAt(now) // 발행 시간
                .addClaims(claims)
                .setExpiration(new Date(now.getTime() + expireTime)) // 유효 기간
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

    @Getter
    @AllArgsConstructor
    public static class JwtInfo {
        private final String username;
        private final RoleType roleType;

        public static JwtInfo of(Member member) {
            return new JwtInfo(member.getUsername(), member.getRole());
        }
    }
}
