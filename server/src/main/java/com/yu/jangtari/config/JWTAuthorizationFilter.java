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
    private final SkipPathRequestMatcher skipPathRequestMatcher;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CookieUtil cookieUtil, JWTUtil jwtUtil, SkipPathRequestMatcher skipPathRequestMatcher) {
        super(authenticationManager);
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.skipPathRequestMatcher = skipPathRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        // accessCookie, refreshCookie 중 하나라도 없다면 InvalidTokenException 발생
        Cookie accessCookie = cookieUtil.getAccessCookie(request);
        Cookie refreshCookie = cookieUtil.getRefreshCookie(request);
        try {
            // accessToken이 정상적이라면 authentication 세팅
            String accessToken = accessCookie.getValue();
            JWTUtil.JwtInfo jwtInfo = jwtUtil.parseJwt(accessToken); // 만약 accessToken이 만료되었다면 아래 ExpiredJwtException에서 잡힘
            Authentication authentication = getAuthenticationFromJwtInfo(jwtInfo);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException accessTokenExpired) {
            // accessToken이 만료되었다면 refreshToken으로 작업
            String refreshToken = refreshCookie.getValue();
            try {
                // refreshToken이 정상적이라면 accessCookie 재설정 후 authentication 세팅
                JWTUtil.JwtInfo jwtInfo = jwtUtil.parseJwt(refreshToken);
                String newAccessToken = jwtUtil.recreateAccessToken(refreshToken);
                Cookie newAccessCookie = cookieUtil.createAccessCookie(newAccessToken);
                response.addCookie(newAccessCookie);
                Authentication authentication = getAuthenticationFromJwtInfo(jwtInfo);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (ExpiredJwtException refreshTokenExpired) {
                // refreshToken도 만료되었다면 재로그인 유도
                accessCookie = cookieUtil.getLogoutCookie(accessCookie);
                refreshCookie = cookieUtil.getLogoutCookie(refreshCookie);
                SecurityContextHolder.getContext().setAuthentication(null);
                response.addCookie(accessCookie);
                response.addCookie(refreshCookie);
                tokenError(response, ErrorCode.RE_LOGIN_ERROR);
            } catch (Exception refreshTokenException) {
                // refreshToken에 뭔가 이상이 있다면 에러 리턴
                log.error("** AUTHENTICATION FAILED : " + refreshTokenException);
                tokenError(response, ErrorCode.INVALID_TOKEN_ERROR);
            }
        } catch (Exception accessTokenException) {
            // accessToken이 애초에 잘못된 토큰이라면 에러 리턴
            log.error("** AUTHENTICATION FAILED : " + accessTokenException);
            tokenError(response, ErrorCode.INVALID_TOKEN_ERROR);
        }
    }

    private Authentication getAuthenticationFromJwtInfo(JWTUtil.JwtInfo jwtInfo) {
        return new UsernamePasswordAuthenticationToken(jwtInfo.getUsername()
            , null
            , Collections.singletonList(() -> "ROLE_" + jwtInfo.getRoleType().name()));
    }

    private void tokenError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        final String responseJson = objectMapper.writeValueAsString(GlobalExceptionHandler.buildError(errorCode));
        response.setStatus(errorCode.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(responseJson);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipPathRequestMatcher.matches(request);
    }
}
