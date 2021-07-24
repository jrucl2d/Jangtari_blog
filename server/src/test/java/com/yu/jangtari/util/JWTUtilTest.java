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
        JwtAndCookieInfo jwtAndCookieInfo = new JwtAndCookieInfo();
        jwtUtil = new JWTUtil(jwtAndCookieInfo);
    }

    @Test
    @DisplayName("access token을 정상적으로 생성")
    void createAccessToken()
    {
        // given
        String username = "jangtari";
        String token = jwtUtil.createAccessToken(username, RoleType.USER);

        // when
        String getUsername = jwtUtil.getUsernameFromJWT(token);
        RoleType roleType = jwtUtil.getRoleFromJWT(token);

        // then
        assertEquals(username, getUsername);
        assertEquals(RoleType.USER, roleType);
    }
    @Test
    @DisplayName("refresh token을 정상적으로 생성")
    void createRefreshToken()
    {
        // given
        String username = "jangtari";
        String token = jwtUtil.createRefreshToken(username, RoleType.ADMIN);

        // when
        String getUsername = jwtUtil.getUsernameFromJWT(token);
        RoleType roleType = jwtUtil.getRoleFromJWT(token);

        // then
        assertEquals(username, getUsername);
        assertEquals(RoleType.ADMIN, roleType);
    }
    @Test
    @DisplayName("만료된 토큰 validate 할 시 ExpiredJwtException 발생")
    void validateToken()
    {
        // given
        JwtAndCookieInfo jwtAndCookieInfo = new TestJwtAndCookieInfo();
        JWTUtil jwtUtil = new JWTUtil(jwtAndCookieInfo);

        // when
        String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.USER);
        String refreshToken = jwtUtil.createAccessToken("jangtari", RoleType.USER);

        // then
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(accessToken));
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(refreshToken));
    }
    @Test
    @DisplayName("이상한 토큰 validate 할 시 MalformedJwtException 발생")
    void validateToken1()
    {
        // given
        String wrongToken = "wlkgjwelkgjwelkgjwelgjwelkgjwe";

        // when
        // then
        assertThrows(MalformedJwtException.class, () -> jwtUtil.validateToken(wrongToken));
    }

    private static class TestJwtAndCookieInfo extends JwtAndCookieInfo {
        @Override
        public int getAccessTokenValidTime() {
            return 0;
        }

        @Override
        public int getRefreshTokenValidTime() {
            return 0;
        }
    }
}