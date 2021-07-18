package com.yu.jangtari.common;

import com.yu.jangtari.domain.RoleType;
import com.yu.jangtari.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JwtToken {
    private final JWTUtil jwtUtil;

    private String token;
    public JwtToken(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getToken() {
        return token;
    }

    public JwtToken getToken(String token) {
        this.token = token;
        return this;
    }

    public JwtToken createAccessToken(String username, RoleType role) {
        this.token = jwtUtil.createAccessToken(username, role);
        return this;
    }
    // refreshToken으로부터 accessToken 재생성
    public JwtToken createAccessToken() {
        final String username = getUsername();
        final RoleType role = getRole();
        return createAccessToken(username, role);
    }
    public JwtToken createRefreshToken(String username, RoleType role) {
        this.token = jwtUtil.createRefreshToken(username, role);
        return this;
    }
    public String getUsername() {
        return jwtUtil.getUsernameFromJWT(this.token);
    }
    public RoleType getRole() {
        return jwtUtil.getRoleFromJWT(this.token);
    }

    public void validation() throws ExpiredJwtException,
        UnsupportedJwtException,
        MalformedJwtException,
        SignatureException,
        IllegalArgumentException
    {
        jwtUtil.validateToken(this.token);
    }
}
