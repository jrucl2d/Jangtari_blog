package com.yu.jangtari.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * '/login' 으로 {username, password}의 json 데이터가 넘어오면 이를 바탕으로 토큰을 생성
 * attemptAuthentication 이후 {@link CustomUserDetailService}의 loadUserByUsername() 메소드가 실행됨
 * 인증에 성공하면 {@link LoginSuccessHandler} 에서, 인증에 실패하면 {@link LoginFailureHandler} 에서 처리
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto.LogInForm loginForm = objectMapper.readValue(request.getInputStream(), MemberDto.LogInForm.class);

        log.info("** 로그인 시도 : " + loginForm.getUsername());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        return authenticationManager.authenticate(token);
    }
}
