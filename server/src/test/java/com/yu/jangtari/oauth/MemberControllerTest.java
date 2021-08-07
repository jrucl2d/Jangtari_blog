package com.yu.jangtari.oauth;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTest
{
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;

    @Test
    @DisplayName("회원가입이 정상적으로 실행된다.")
    void join() throws Exception
    {
        // given
        MemberDto.Add dto = MemberDto.Add.builder()
            .username("jangtari")
            .nickname("jangtari")
            .password("1234")
            .build();

        // when
        String content = objectMapper.writeValueAsString(dto);

        // then
        mockMvc.perform(post("/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value("OK"))
            .andDo(print());
    }

    @Test
    @DisplayName("이미 가입된 회원이 있으면 에러 발생")
    void join_X() throws Exception
    {
        // given
        MemberDto.Add dto = MemberDto.Add.builder()
            .username("jangtari")
            .nickname("jangtari")
            .password("1234")
            .build();
        String content = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        // when
        content = objectMapper.writeValueAsString(dto);

        // then
        mockMvc.perform(post("/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER_ERROR.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("로그인하면 accessCookie와 refreshCookie가 생성된다.")
    void login() throws Exception
    {
        // given
        MemberDto.Add dto = MemberDto.Add.builder()
            .username("jangtari")
            .nickname("jangtari")
            .password("1234")
            .build();
        String content = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        LoginForm loginForm = new LoginForm("jangtari", "1234");

        // when
        content = objectMapper.writeValueAsString(loginForm);

        // then
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("accessCookie"))
            .andExpect(cookie().exists("refreshCookie"))
            .andDo(print());
    }

    @Test
    @DisplayName("없는 아이디로 로그인하면 에러 발생")
    void login_X() throws Exception
    {
        // given
        LoginForm loginForm = new LoginForm("jangtari", "1234");

        // when
        String content = objectMapper.writeValueAsString(loginForm);

        // then
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value(ErrorCode.MEMBER_NOT_FOUND.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.MEMBER_NOT_FOUND.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("비밀번호를 틀리면 에러 발생")
    void login_X1() throws Exception
    {
        // given
        MemberDto.Add dto = MemberDto.Add.builder()
            .username("jangtari")
            .nickname("jangtari")
            .password("1234")
            .build();
        String content = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content));

        LoginForm loginForm = new LoginForm("jangtari", "1111222");

        // when
        content = objectMapper.writeValueAsString(loginForm);

        // then
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value(ErrorCode.MEMBER_NOT_FOUND.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.MEMBER_NOT_FOUND.getCode()))
            .andDo(print());
    }

    private static class LoginForm {
        private final String username;
        private final String password;
        public LoginForm(String username, String password) {
            this.username = username;
            this.password = password;
        }
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
    }


    @Test
    @DisplayName("정상적으로 로그아웃 수행")
    void logout() throws Exception
    {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("jangtari", null, Collections.singletonList(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtUtil.createAccessToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtUtil.createRefreshToken(new JwtUtil.JwtInfo("jangtari", RoleType.USER)));

        // when

        // then
        mockMvc.perform(post("/logout").cookie(accessCookie, refreshCookie))
            .andExpect(status().isOk())
            .andExpect(cookie().maxAge("accessCookie", 0))
            .andExpect(cookie().maxAge("refreshCookie", 0))
            .andDo(print());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("쿠키가 없는 상태에서 로그아웃 하려고 하면 InvalidTokenException 발생")
    void logout_X() throws Exception
    {
        // given
        // when
        // then
        mockMvc.perform(post("/logout"))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TOKEN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TOKEN_ERROR.getCode()))
            .andDo(print());
    }
}