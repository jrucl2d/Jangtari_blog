package com.yu.jangtari.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.common.ErrorCode;
import com.yu.jangtari.common.GlobalExceptionHandler;
import lombok.*;
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
            log.info("** LOGIN ATTEMPT : " + loginForm.getUsername());
            token = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        } catch (IOException e) {
            log.error("IOException Occurred while login process");
        }
        return authenticationManager.authenticate(token); // UserDetailsService의 loadUserByUsername() 실행
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final String username = authResult.getName();
        final String role = (String) authResult.getAuthorities().toArray()[0];
        log.info("** LOGIN SUCCESS : " + authResult.getName());
        final String accessToken = jwtUtil.createAccessToken(username, role);
        final Cookie accessCookie = cookieUtil.createAccessCookie(accessToken);
        final String refreshToken = jwtUtil.createRefreshToken(username, role);
        final Cookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("** LOGIN FAILED : " + failed);
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
