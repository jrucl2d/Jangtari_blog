package com.yu.jangtari.util;

import com.yu.jangtari.domain.RoleType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {
    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp()
    {
        jwtUtil = new JWTUtil();
    }

    @Test
    @DisplayName("access token이 정상적으로 생성되고 parsing 됨")
    void createAccessToken()
    {
        // given
        String username = "jangtari";
        JWTUtil.JwtInfo jwtInfo = new JWTUtil.JwtInfo(username, RoleType.USER);
        String token = jwtUtil.createAccessToken(jwtInfo);

        // when
        JWTUtil.JwtInfo gotJwtInfo = jwtUtil.parseJwt(token);

        // then
        assertEquals(username, gotJwtInfo.getUsername());
        assertEquals(RoleType.USER, gotJwtInfo.getRoleType());
    }

    @Test
    @DisplayName("refresh token이 정상적으로 생성되고 parsing됨")
    void createRefreshToken()
    {
        // given
        String username = "jangtari";
        JWTUtil.JwtInfo jwtInfo = new JWTUtil.JwtInfo(username, RoleType.ADMIN);
        String token = jwtUtil.createRefreshToken(jwtInfo);

        // when
        JWTUtil.JwtInfo gotJwtInfo = jwtUtil.parseJwt(token);

        // then
        assertEquals(username, gotJwtInfo.getUsername());
        assertEquals(RoleType.ADMIN, gotJwtInfo.getRoleType());
    }

    @Test
    @DisplayName("accessToken이 만료된 경우 refreshToken으로부터 새로운 accessToken을 재생성")
    void recreateAccessToken()
    {
        // given
        String username = "jangtari";
        JWTUtil.JwtInfo jwtInfo = new JWTUtil.JwtInfo(username, RoleType.ADMIN);
        String refreshToken = jwtUtil.createRefreshToken(jwtInfo);

        // when
        String newAccessToken = jwtUtil.recreateAccessToken(refreshToken);
        JWTUtil.JwtInfo gotJwtInfo = jwtUtil.parseJwt(newAccessToken);

        // then
        assertEquals(username, gotJwtInfo.getUsername());
        assertEquals(RoleType.ADMIN, gotJwtInfo.getRoleType());
    }

    @Test
    @DisplayName("만료된 토큰 parsing 할 시 ExpiredJwtException 발생")
    void parseJwt_X()
    {
        // given
        JWTUtil jwtUtil = new JWTUtil();
        String username = "jangtari";
        JWTUtil.JwtInfo jwtInfo = new JWTUtil.JwtInfo(username, RoleType.USER);

        // when
        String accessToken = jwtUtil.createToken(jwtInfo, 10);
        String refreshToken = jwtUtil.createToken(jwtInfo, 10);

        // then
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.parseJwt(accessToken));
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.parseJwt(refreshToken));
    }

    @Test
    @DisplayName("이상한 토큰 parsing 할 시 MalformedJwtException 발생")
    void parseJwt_X1()
    {
        // given
        String wrongToken = "wlkgjwelkgjwelkgjwelgjwelkgjwe";

        // when
        // then
        assertThrows(MalformedJwtException.class, () -> jwtUtil.parseJwt(wrongToken));
    }
}