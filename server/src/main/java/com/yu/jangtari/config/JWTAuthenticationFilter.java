package com.yu.jangtari.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.common.ErrorCode;
import com.yu.jangtari.common.GlobalExceptionHandler;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JWTUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * '/login' 으로 {username, password}의 json 데이터가 넘어오면 이를 바탕으로 토큰을 생성
 * attemptAuthentication 이후 {@link CustomUserDetailService}의 loadUserByUsername() 메소드가 실행됨
 * 그 결과는 successfulAuthentication 혹은 unsuccessfulAuthentication에서 받아볼 수 있음
 */
// /login으로 post 방식의 {username, password} 요청시 동작하는 UsernamePasswordAuthenticationFilter를 설정
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginForm loginForm = objectMapper.readValue(request.getInputStream(), LoginForm.class);
            log.info("** 로그인 시도 : " + loginForm.getUsername());
            token = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        } catch (IOException e) {
            log.error("로그인 시도 중 IOException이 발생했습니다.");
        }
        return authenticationManager.authenticate(token); // CustomUserDetailService의 loadUserByUsername() 실행
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        Member member = customUserDetail.getMember();
        log.info("** 로그인 성공 : " + member.getUsername());

        JWTUtil.JwtInfo jwtInfo = JWTUtil.JwtInfo.getInstance(member);

        String accessToken = jwtUtil.createAccessToken(jwtInfo);
        Cookie accessCookie = cookieUtil.createAccessCookie(accessToken);
        String refreshToken = jwtUtil.createRefreshToken(jwtInfo);
        Cookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("** 로그인 실패 : ", failed);
        final ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        ObjectMapper objectMapper = new ObjectMapper();
        final String responseJson = objectMapper.writeValueAsString(GlobalExceptionHandler.buildError(errorCode));
        response.setStatus(errorCode.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(responseJson);
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class LoginForm {
        private String username;
        private String password;
    }
}
