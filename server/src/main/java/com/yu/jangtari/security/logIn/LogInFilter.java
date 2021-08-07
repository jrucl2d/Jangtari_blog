package com.yu.jangtari.security.logIn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.GlobalExceptionHandler;
import com.yu.jangtari.security.CustomUserDetail;
import com.yu.jangtari.security.CustomUserDetailService;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JWTUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
 * 인증에 성공하면 {@link LogInSuccessHandler} 에서, 인증에 실패하면 {@link LogInFailureHandler} 에서 처리
 */
@Slf4j
@RequiredArgsConstructor
public class LogInFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginForm loginForm = objectMapper.readValue(request.getInputStream(), LoginForm.class);
        log.info("** 로그인 시도 : " + loginForm.getUsername());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        return authenticationManager.authenticate(token);
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
