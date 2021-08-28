package com.yu.jangtari.api.member.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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
            .andExpect(jsonPath("$").value("회원가입 성공"))
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
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATED_MEMBER_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_MEMBER_ERROR.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("로그인하면 accessToken, refreshToken 생성된다.")
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

        MemberDto.LogInForm loginForm = new MemberDto.LogInForm("jangtari", "1234");

        // when
        content = objectMapper.writeValueAsString(loginForm);

        // then
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("로그인 성공"))
            .andExpect(header().exists("Authorization"))
            .andExpect(header().exists("RefreshToken"))
            .andDo(print());
    }

    @Test
    @DisplayName("없는 아이디로 로그인하면 에러 발생")
    void login_X() throws Exception
    {
        // given
        MemberDto.LogInForm loginForm = new MemberDto.LogInForm("jangtari", "1234");

        // when
        String content = objectMapper.writeValueAsString(loginForm);

        // then
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.LOGIN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.LOGIN_ERROR.getCode()))
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

        MemberDto.LogInForm loginForm = new MemberDto.LogInForm("jangtari", "1111");

        // when
        content = objectMapper.writeValueAsString(loginForm);

        // then
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.LOGIN_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.LOGIN_ERROR.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("정상적으로 로그아웃 수행")
    void logout() throws Exception
    {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken("jangtari", null, Collections.singletonList(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtInfo jwtInfo = JwtInfo.builder()
            .memberId(1L)
            .username("username")
            .nickName("nick")
            .roleType(RoleType.USER)
            .build();
        String accessToken = JwtUtil.createAccessToken(jwtInfo);

        // when
        // then
        mockMvc.perform(get("/user/logout")
            .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("로그아웃 성공"))
            .andDo(print());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(refreshTokenRepository.findById("jangtari")).isNotPresent();
    }

    @Test
    @DisplayName("토큰이 없는 상태에서 로그아웃 하려고 하면 401 권한 없음")
    void logout_X() throws Exception
    {
        // given
        // when
        // then
        mockMvc.perform(get("/user/logout"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ErrorCode.JWT_VALIDATION_ERROR.getMessage()))
            .andExpect(jsonPath("$.code").value(ErrorCode.JWT_VALIDATION_ERROR.getCode()))
            .andDo(print());
    }
}