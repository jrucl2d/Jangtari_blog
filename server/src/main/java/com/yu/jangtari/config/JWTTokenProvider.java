package com.yu.jangtari.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JWTTokenProvider {

    public static final long ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000L; // Access token 2분
    public final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // Refresh token 1주일

    public final String ACCESS_TOKEN_STRING = "accesstest";
    public final String REFRESH_TOKEN_STRING = "refreshtest";

    private String secretKey = "test";
    private UserDetailsService userDetailsService;

    @Autowired
    protected JWTTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    // 객체 초기화시에 secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private String createToken(String userPk, Long theTime) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload에 저장되는 정보
        claims.put("username", userPk); // key-value 쌍으로 저장됨
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 발행 시간
                .setExpiration(new Date(now.getTime() + theTime)) // 유효 기간
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘
                .compact();
    }

    public String createAccessToken(String userPK){
        return createToken(userPK, ACCESS_TOKEN_VALID_TIME);
    }
    public String createRefreshToken(String userPK){
        return createToken(userPK, REFRESH_TOKEN_VALID_TIME);
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    // 토큰에서 회원정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    // Request Header에서 token 가져오기
    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN"); // "X-AUTH-TOKEN" : "토큰 값"
    }
    public String resolveRefreshToken(HttpServletRequest request){
        return request.getHeader("REFRESH-TOKEN");
    }

    // 토큰의 유효성, 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e){
            return false;
        }
    }
}
