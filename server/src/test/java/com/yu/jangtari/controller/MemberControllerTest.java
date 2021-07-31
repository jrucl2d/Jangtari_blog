package com.yu.jangtari.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.common.ErrorCode;
import com.yu.jangtari.domain.DTO.MemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTest
{
    @Test
    @DisplayName("회원가입이 정상적으로 실행된다.")
    void join() throws Exception
    {
        // given
        MemberDTO.Add dto = MemberDTO.Add.builder()
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
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("OK"))
            .andDo(print());
    }

    @Test
    @DisplayName("이미 가입된 회원이 있으면 에러 발생")
    void join_X() throws Exception
    {
        // given
        MemberDTO.Add dto = MemberDTO.Add.builder()
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
        MemberDTO.Add dto = MemberDTO.Add.builder()
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
        MemberDTO.Add dto = MemberDTO.Add.builder()
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
}