package com.yu.jangtari.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.common.ErrorCode;
import com.yu.jangtari.common.GlobalExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// BasicAuthenticationFilter는 권한 인증이 필요한 경우에만 동작함
@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CookieUtil cookieUtil, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        final Cookie[] cookies = request.getCookies();
        try {
            // 1. cookie(token)이 존재하지 않다면 아래 Error 리턴
            if (cookies == null || cookies[0] == null) throw new JwtException("NO JWT TOKEN!!");

            // 2.1. accessToken이 유효하면 정상 종료
            final String accessToken = cookies[0].getValue();
            jwtUtil.validateToken(accessToken);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 2.2. accessToken이 만료된 것이라면 refreshToken check
            final String refreshToken = cookies[1].getValue();
            try {
                // 3.1. refreshToken이 유효하면 accessToken 재발급 후 정상 종료
                jwtUtil.validateToken(refreshToken);
                final Cookie newAccessCookie = recreateAccessCookie(refreshToken);
                response.addCookie(newAccessCookie);
                chain.doFilter(request, response);
            } catch (ExpiredJwtException | ServletException eee) {
                // 3.2. refreshToken이 만료되었거나 문제가 있다면 Error 리턴, 재 로그인 유도
                log.error("** AUTHENTICATION FAILED : " + eee);
                tokenError(response, ErrorCode.INVALID_REFRESH_TOKEN);
            }
        } catch (Exception ee) {
            log.error("** AUTHENTICATION FAILED : " + ee);
            tokenError(response, ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
    private Cookie recreateAccessCookie(String refreshToken) {
        final String username = jwtUtil.getUsernameFromJWT(refreshToken);
        final String accessToken = jwtUtil.createAccessToken(username);
        return cookieUtil.createCookie(true, accessToken);
    }
    private void tokenError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        final String responseJson = objectMapper.writeValueAsString(GlobalExceptionHandler.buildError(errorCode));
        response.setStatus(errorCode.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(responseJson);
    }
}
