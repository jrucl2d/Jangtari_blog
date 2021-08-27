package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest
{
    private static JwtInfo jwtInfo;

    @BeforeAll
    static void beforeAll() {
        jwtInfo = new JwtInfo(1L, "username", RoleType.ADMIN);
    }

    @Nested
    @DisplayName("Access Token 테스트")
    class AccessToken {
        @Test
        @DisplayName("access token이 정상적으로 생성됨")
        void createAccessToken()
        {
            // given
            // when
            String token = JwtUtil.createAccessToken(jwtInfo);

            // then
            assertNotNull(token);
        }

        @Test
        @DisplayName("access token이 정상적으로 decoding 됨")
        void decodeAccessToken()
        {
            // given
            String token = JwtUtil.createAccessToken(jwtInfo);

            // when
            JwtInfo decodedInfo = JwtUtil.decodeAccessToken(token);

            // then
            assertThat(decodedInfo).isEqualTo(jwtInfo);
        }

        @Test
        @DisplayName("null인 accessToken을 decode하려고 하면 IllegalArgumentException이 발생함")
        void accessTokenDoesNotExist()
        {
            // given
            String token = null;

            // when
            // then
            assertThrows(IllegalArgumentException.class, () -> JwtUtil.decodeAccessToken(token));
        }

        @Test
        @DisplayName("잘못된 토큰으로 decoding하면 validation exception이 발생한다.")
        void validationCheck()
        {
            // given
            String invalidAuthToken = "invalidToken";
            String expiredToken = JwtUtil.createAccessToken(jwtInfo, 0);

            // when
            // then
            assertThrows(MalformedJwtException.class, () -> JwtUtil.decodeAccessToken(invalidAuthToken));
            assertThrows(ExpiredJwtException.class, () -> JwtUtil.decodeAccessToken(expiredToken));
        }
    }

    @Nested
    @DisplayName("Refresh Token 테스트")
    class RefreshToken
    {
        @Test
        @DisplayName("refresh token이 정상적으로 생성됨")
        void createRefreshToken()
        {
            // given
            String accessToken = "accessToken";

            // when
            String refreshToken = JwtUtil.createRefreshToken(accessToken);

            // then
            assertNotNull(refreshToken);
        }

        @Test
        @DisplayName("refresh token이 정상적으로 decoding 됨")
        void decodeRefreshToken()
        {
            // given
            String accessToken = JwtUtil.createAccessToken(jwtInfo, 0);
            String refreshToken = JwtUtil.createRefreshToken(accessToken);

            // when
            JwtInfo decodedInfo = JwtUtil.decodeRefreshToken(refreshToken);

            // then
            assertThat(decodedInfo).isEqualTo(jwtInfo);
        }

        @Test
        @DisplayName("null인 refreshToken decode하려고 하면 IllegalArgumentException이 발생함")
        void accessTokenDoesNotExist()
        {
            // given
            String token = null;

            // when
            // then
            assertThrows(IllegalArgumentException.class, () -> JwtUtil.decodeRefreshToken(token));
        }

        @Test
        @DisplayName("잘못된 refresh token으로 decoding하면 validation exception이 발생한다.")
        void validationCheck()
        {
            // given
            String invalidAuthToken = "invalidToken";
            String expiredToken = JwtUtil.createRefreshToken(invalidAuthToken, 0);

            // when
            // then
            assertThrows(MalformedJwtException.class, () -> JwtUtil.decodeRefreshToken(invalidAuthToken));
            assertThrows(ExpiredJwtException.class, () -> JwtUtil.decodeRefreshToken(expiredToken));
        }
    }
}