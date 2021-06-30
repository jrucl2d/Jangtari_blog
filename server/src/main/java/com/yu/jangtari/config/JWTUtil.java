package com.yu.jangtari.config;

import com.google.api.client.util.Value;
import com.yu.jangtari.common.exception.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTUtil {
    public static final long ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000L; // Access token 2분
    public static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // Refresh token 1주일
    public static final String ACCESS_TOKEN_STRING = "accessstring";
    public static final String REFRESH_TOKEN_STRING = "refreshstring";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${spring.jwt.secret}")
    private String JWT_SECRET_KEY;

    private final UserDetailsService userDetailsService;

    // 객체 초기화시에 secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        JWT_SECRET_KEY = Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes());
    }

    // JWT 토큰 생성 관련 메소드
    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_VALID_TIME);
    }
    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_VALID_TIME);
    }
    private String createToken(final String username, long expireTime) {
        final Date now = new Date();
        final Claims claims = Jwts.claims(); // JWT payload에 저장되는 정보 claim
        claims.put("username", username); // key-value 쌍으로 저장됨

        return TOKEN_PREFIX + Jwts.builder()
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
    public Authentication getAuthentication(final String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 차례대로 principal(본인), credentials(자격증명, 비밀번호), 권한
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
    private Jws<Claims> getClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);
    }

    // Request Header에서 token 가져오기
    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization"); // "Authorization" : "토큰 값"
    }
    public String resolveRefreshToken(HttpServletRequest request){
        return request.getHeader("RefreshToken"); // "RefreshToken" : "토큰 값"
    }

    // 토큰의 유효성, 만료일자 확인
    public boolean validateToken(final String token) {
        if (!isStartsWithBearer(token)) return false;
        String extractedToken = token.replace(TOKEN_PREFIX, "");
        return isNotExpired(extractedToken);
    }
    private boolean isStartsWithBearer(final String token) {
        return token.startsWith(TOKEN_PREFIX);
    }
    private boolean isNotExpired(final String token) {
        try {
            final Jws<Claims> claims = getClaims(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
