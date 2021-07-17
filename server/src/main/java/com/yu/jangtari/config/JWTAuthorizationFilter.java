package com.yu.jangtari.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.common.ErrorCode;
import com.yu.jangtari.common.GlobalExceptionHandler;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

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
        final Cookie accessCookie = cookieUtil.getCookie(request, CookieUtil.ACCESS_COOKIE_NAME);
        final Cookie refreshCookie = cookieUtil.getCookie(request, CookieUtil.REFRESH_COOKIE_NAME);
        try {
            // 1. cookie(token)이 존재하지 않다면 통과 -> 에러 리턴
            if (accessCookie == null) {
                chain.doFilter(request, response);
                return;
            }

            // 2.1. accessToken이 유효하면 정상 종료
            final String accessToken = accessCookie.getValue();
            jwtUtil.validateToken(accessToken);
            final String username = jwtUtil.getUsernameFromJWT(accessToken);
            final String roleType = jwtUtil.getRoleFromJWT(accessToken);
            final Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList((GrantedAuthority) () -> "ROLE_" + roleType));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 2.2. accessToken이 만료된 것이라면 refreshToken check
            final String refreshToken = refreshCookie.getValue();
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
        final String roleType = jwtUtil.getRoleFromJWT(refreshToken);
        final String accessToken = jwtUtil.createAccessToken(username, roleType);
        return cookieUtil.createAccessCookie(accessToken);
    }
    private void tokenError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        final String responseJson = objectMapper.writeValueAsString(GlobalExceptionHandler.buildError(errorCode));
        response.setStatus(errorCode.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(responseJson);
    }
}
