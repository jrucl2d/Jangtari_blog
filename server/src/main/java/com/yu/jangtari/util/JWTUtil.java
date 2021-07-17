package com.yu.jangtari.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTUtil {
    public static final int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000; // Access token 2분
    public static final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일

//    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY = "tmp";

    private final UserDetailsService userDetailsService;

    // 객체 초기화시에 secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        JWT_SECRET_KEY = Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes());
    }

    // JWT 토큰 생성 관련 메소드
    public String createAccessToken(String username, String role) {
        return createToken(username, role, ACCESS_TOKEN_VALID_TIME);
    }
    public String createRefreshToken(String username, String role) {
        return createToken(username, role, REFRESH_TOKEN_VALID_TIME);
    }
    private String createToken(final String username, final String role, long expireTime) {
        final Date now = new Date();
        final Claims claims = Jwts.claims(); // JWT payload에 저장되는 정보 claim
        claims.put("username", username); // key-value 쌍으로 저장됨
        claims.put("role", role);

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
    public String getRoleFromJWT(final String token) {
        return getClaims(token).getBody().get("role", String.class);
    }
    public Authentication getAuthentication(final String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 차례대로 principal(본인), credentials(자격증명, 비밀번호), 권한
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
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
