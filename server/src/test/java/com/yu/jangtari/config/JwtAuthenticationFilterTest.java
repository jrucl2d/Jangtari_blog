package com.yu.jangtari.config;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationFilterTest extends IntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;

    @Test
    @DisplayName("제대로 된 accessToken으로 통과")
    void authWithAccessToken() throws Exception
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtUtil.createAccessToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtUtil.createRefreshToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));

        // when
        // then
        mockMvc.perform(get("/user/test")
            .cookie(accessCookie, refreshCookie))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("accessCookie"))
            .andExpect(cookie().exists("refreshCookie"))
            .andExpect(jsonPath("$").value("user"))
            .andDo(print());
    }

    @Test
    @DisplayName("accessToken이 만료되었으면 refreshToken으로 통과")
    void authWithAccessToken1() throws Exception
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtUtil.createToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER), 10));
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtUtil.createRefreshToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));

        // when
        // then
        mockMvc.perform(get("/user/test")
            .cookie(accessCookie, refreshCookie))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("accessCookie"))
            .andExpect(cookie().exists("refreshCookie"))
            .andExpect(jsonPath("$").value("user"))
            .andDo(print());
    }

    @Test
    @DisplayName("쿠키가 없으면 요청 실패")
    void authWithAccessToken_X() throws Exception
    {
        // given
        // when
        // then
        mockMvc.perform(get("/admin/test"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TOKEN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TOKEN_ERROR.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("제대로 된 accessToken이지만 USER 권한으로 ADMIN 요청하면 실패")
    void authWithAccessToken_X1() throws Exception
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtUtil.createAccessToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtUtil.createRefreshToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));

        // when
        // then
        mockMvc.perform(get("/admin/test")
            .cookie(accessCookie, refreshCookie))
            .andExpect(status().isForbidden())
            .andDo(print());
    }

    @Test
    @DisplayName("잘못된 accessToken으로 요청시 실패")
    void authWithAccessToken_X2() throws Exception
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie("noway");
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtUtil.createRefreshToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));

        // when
        // then
        mockMvc.perform(get("/user/test")
            .cookie(accessCookie, refreshCookie))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TOKEN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TOKEN_ERROR.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("accessToken은 만료됐고 refreshToken은 잘못되었을 때 실패")
    void authWithAccessToken_X3() throws Exception
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtUtil.createToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER), 10));
        Cookie refreshCookie = cookieUtil.createRefreshCookie("noway");

        // when
        // then
        mockMvc.perform(get("/user/test")
            .cookie(accessCookie, refreshCookie))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TOKEN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TOKEN_ERROR.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("accessToken은 만료됐고 refreshToken도 만료됐을 때 재로그인을 요청하며 실패")
    void authWithAccessToken_X4() throws Exception
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtUtil.createToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER), 10));
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtUtil.createToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER), 10));

        // when
        // then
        mockMvc.perform(get("/user/test")
            .cookie(accessCookie, refreshCookie))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.RE_LOGIN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.RE_LOGIN_ERROR.getCode()))
            .andDo(print());
    }
}