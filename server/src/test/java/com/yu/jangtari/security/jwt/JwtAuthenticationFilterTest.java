package com.yu.jangtari.security.jwt;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationFilterTest extends IntegrationTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final JwtInfo jwtInfo = JwtInfo.builder()
        .username("username")
        .nickName("nick")
        .roleType(RoleType.USER)
        .build();

    @Test
    @DisplayName("아무런 토큰 없이 권한 필요하지 않은 path 요청하면 401 UnAuthorized 에러")
    void unAuthorized() throws Exception
    {
        // given
        // when
        // then
        mockMvc.perform(get("/nowhere"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
            .andDo(print());
    }

    @Test
    @DisplayName("아무런 토큰 없이 권한 필요한 path 요청하면 401 UnAuthorized 에러")
    void accessDenied() throws Exception
    {
        // given
        // when
        // then
        mockMvc.perform(get("/admin/test"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
            .andDo(print());
    }

    @Nested
    @DisplayName("accessToken으로 요청")
    class AccessToken
    {
        @Test
        @DisplayName("refreshToken을 Authorization 헤더(accessToken 헤더)에 넣어서 보낼 경우 401 UnAuthorized 에러")
        void refreshTokenInAuthorizationHeader() throws Exception
        {
            // given
            String accessToken = JwtUtil.createAccessToken(jwtInfo);
            String refreshToken = JwtUtil.createRefreshToken(accessToken);

            // when
            // then
            mockMvc.perform(get("/user/test")
                .header(HttpHeaders.AUTHORIZATION, refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("filter 정상적으로 통과 후 SecurityContext를 통해 요청한 사용자 정보 접근 가능")
        void accessTokenFilterPassSuccess() throws Exception
        {
            // given
            String token = JwtUtil.createAccessToken(jwtInfo);

            // when
            // then
            mockMvc.perform(get("/user/test")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(jwtInfo.getUsername()))
                .andExpect(jsonPath("$.roleType").value(jwtInfo.getRoleType().name()))
                .andDo(print());
        }

        @Test
        @DisplayName("user 권한으로 admin 요청 보내면 403 Access Denied 에러")
        void accessDenied() throws Exception
        {
            // given
            String token = JwtUtil.createAccessToken(jwtInfo);
            // when
            // then
            mockMvc.perform(get("/admin/test")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCESS_DENIED_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCESS_DENIED_ERROR.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("Access Token이 만료되었을 때 오류 메세지 return, 401 UnAuthorized 에러")
        void accessTokenExpired() throws Exception
        {
            // given
            String token = JwtUtil.createAccessToken(jwtInfo, 0);

            // when
            // then
            mockMvc.perform(get("/user/test")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_TIMEOUT_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_TIMEOUT_ERROR.getMessage()))
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("refreshToken으로 요청")
    class RefreshToken
    {
        @Test
        @DisplayName("refreshToken decode에서 에러가 발생되면 401 UnAuthorized 에러")
        void refreshTokenUnAuthorized() throws Exception
        {
            // given
            String refreshToken = "asfd.asfd.fsad";

            // when
            // then
            mockMvc.perform(get("/user/test")
                .header(JwtUtil.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("refreshToken 안의 accessToken에서 에러가 발생되면 401 UnAuthorized 에러")
        void accessTokenInRefreshTokenUnAuthorized() throws Exception
        {
            // given
            String refreshToken = JwtUtil.createRefreshToken("wlgkjwelgkjweglkwejg.wlekgjwelkg.lwkjeglk");

            // when
            // then
            mockMvc.perform(get("/api/partner/test")
                .header(JwtUtil.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("refreshToken이 만료되었을 때 다시 로그인 하라는 메시지 return, 401 UnAuthorized 에러")
        void refreshTokenExpired() throws Exception
        {
            // given
            String token = JwtUtil.createRefreshToken("access token", 0);

            // when
            // then
            mockMvc.perform(get("/api/partner/test")
                .header(JwtUtil.REFRESH_TOKEN, token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_TIMEOUT_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_TIMEOUT_ERROR.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("refreshToken이 정상일 경우 accessToken 재발급 후 filter 통과, 사용자 정보 접근 가능")
        void recreateAccessToken() throws Exception
        {
            // given
            String expiredAccessToken = JwtUtil.createAccessToken(jwtInfo, 0);
            String refreshToken = JwtUtil.createRefreshToken(expiredAccessToken, 10);
            refreshTokenRepository.save(
                com.yu.jangtari.api.member.domain.RefreshToken.builder()
                    .username(jwtInfo.getUsername())
                    .refreshToken(refreshToken)
                    .build());

            // when
            // then
            mockMvc.perform(get("/user/test")
                .header(JwtUtil.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(jwtInfo.getUsername()))
                .andExpect(jsonPath("$.roleType").value(jwtInfo.getRoleType().name()))
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION)) // 재발급 받은 accessToken
                .andDo(print());
        }

        @Test
        @DisplayName("db에 저장된 refreshToken과 header의 refreshToken이 맞지 않으면 401 UnAuthorized")
        void recreateAccessTokenFailed() throws Exception
        {
            // given
            String expiredAccessToken = JwtUtil.createAccessToken(jwtInfo, 0);
            String refreshToken = JwtUtil.createRefreshToken(expiredAccessToken, 10);
            refreshTokenRepository.save(
                com.yu.jangtari.api.member.domain.RefreshToken.builder()
                    .username(jwtInfo.getUsername())
                    .refreshToken("no")
                    .build());

            // when
            // then
            mockMvc.perform(get("/api/partner/test")
                .header(JwtUtil.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andDo(print());
        }

        @Test
        @DisplayName("redis에 refreshToken이 저장되어있지 않다면 401 UnAuthorized")
        void recreateAccessTokenFailed1() throws Exception
        {
            // given
            refreshTokenRepository.deleteAll();
            String expiredAccessToken = JwtUtil.createAccessToken(jwtInfo, 0);
            String refreshToken = JwtUtil.createRefreshToken(expiredAccessToken, 10);

            // when
            // then
            mockMvc.perform(get("/api/partner/test")
                .header(JwtUtil.REFRESH_TOKEN, refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andDo(print());
        }
    }
}