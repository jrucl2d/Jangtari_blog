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
    private final SkipPathRequestMatcher skipPathRequestMatcher;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CookieUtil cookieUtil, JWTUtil jwtUtil, SkipPathRequestMatcher skipPathRequestMatcher) {
        super(authenticationManager);
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.skipPathRequestMatcher = skipPathRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        final Cookie accessCookie = cookieUtil.getAccessCookie(request);
        final Cookie refreshCookie = cookieUtil.getRefreshCookie(request);
        try {
            // 1. cookie(token)이 존재하지 않다면 통과 -> 에러 리턴
            if (accessCookie == null) {
                chain.doFilter(request, response);
                return;
            }
            // 2.1. accessToken이 유효하면 정상 종료
//            final JwtToken accessToken = jwtTokenProvider.get().getToken(accessCookie.getValue());
//            accessToken.validation();
//            final String username = accessToken.getUsername();
//            final RoleType roleType = accessToken.getRole();
//            final Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(() -> "ROLE_" + roleType.name()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 2.2. accessToken이 만료된 것이라면 refreshToken check
//            final JwtToken refreshToken = jwtTokenProvider.get().getToken(refreshCookie.getValue());
//            try {
//                // 3.1. refreshToken이 유효하면 accessToken 재발급 후 정상 종료
//                refreshToken.validation();
//                final JwtToken newAccessToken = refreshToken.createAccessToken();
//                final Cookie newAccessCookie = cookieUtil.createAccessCookie(newAccessToken.getToken());
//                response.addCookie(newAccessCookie);
//                chain.doFilter(request, response);
//            } catch (ExpiredJwtException | ServletException eee) {
//                // 3.2. refreshToken이 만료되었거나 문제가 있다면 Error 리턴, 재 로그인 유도
//                log.error("** AUTHENTICATION FAILED : " + eee);
//                tokenError(response, ErrorCode.INVALID_REFRESH_TOKEN);
//            }
        } catch (Exception ee) {
            log.error("** AUTHENTICATION FAILED : " + ee);
            tokenError(response);
        }
    }

    private void tokenError(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        final String responseJson = objectMapper.writeValueAsString(GlobalExceptionHandler.buildError(ErrorCode.INVALID_TOKEN_ERROR));
        response.setStatus(ErrorCode.INVALID_TOKEN_ERROR.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(responseJson);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipPathRequestMatcher.matches(request);
    }
}
