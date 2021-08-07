package com.yu.jangtari.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.GlobalExceptionHandler;
import com.yu.jangtari.common.exception.InvalidTokenException;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JwtUtil;
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

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final SkipPathRequestMatcher skipPathRequestMatcher;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CookieUtil cookieUtil, JwtUtil jwtUtil, SkipPathRequestMatcher skipPathRequestMatcher) {
        super(authenticationManager);
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.skipPathRequestMatcher = skipPathRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            // access token이 정상적일 경우
            String accessToken = cookieUtil.getAccessTokenFromCookie(request);
        }
        catch (ExpiredJwtException accessTokenExpiredException)
        {
            // access token이 만료되었을 경우
            try
            {
                // refresh token이 정상적일 경우 access token 재발급
                NewbieUser newbieUser = setAuthentication(request, "RefreshToken");
                String newAccessToken = jwtUtil.createAccessToken(newbieUser);
                response.setHeader("Authorization", newAccessToken);
            }
            catch (Exception refreshTokenWrongException)
            {
                // refresh token이 없거나 잘못되거나 만료되었을 경우
                log.error("JwtAuthenticationFilter refreshToken error : ", refreshTokenWrongException);
            }
        }
        catch (Exception accessTokenException)
        {
            // access token이 없거나 잘못되었을 경우
            log.error("JwtAuthenticationFilter accessToken error : ", accessTokenException);
        }
        chain.doFilter(request, response);

//        Cookie accessCookie = null;
//        Cookie refreshCookie = null;
//        try
//        {
//            accessCookie = cookieUtil.getAccessCookie(request);
//            refreshCookie = cookieUtil.getRefreshCookie(request);
//            // accessToken이 정상적이라면 authentication 세팅
//            String accessToken = accessCookie.getValue();
//            JwtUtil.JwtInfo jwtInfo = jwtUtil.parseJwt(accessToken); // 만약 accessToken이 만료되었다면 아래 ExpiredJwtException에서 잡힘
//            tokenSuccess(response, accessCookie, refreshCookie, jwtInfo);
//            chain.doFilter(request, response);
//        }
//        catch (InvalidTokenException invalidTokenException)
//        {
//            // Cookie가 없다면 InvalidTokenException
//            tokenError(response, ErrorCode.INVALID_TOKEN_ERROR);
//        }
//        catch (ExpiredJwtException accessTokenExpired)
//        {
//            // accessToken이 만료되었다면 refreshToken으로 작업
//            String refreshToken = refreshCookie.getValue();
//            try
//            {
//                // refreshToken이 정상적이라면 accessCookie 재설정 후 authentication 세팅
//                JwtUtil.JwtInfo jwtInfo = jwtUtil.parseJwt(refreshToken);
//                String newAccessToken = jwtUtil.recreateAccessToken(refreshToken);
//                Cookie newAccessCookie = cookieUtil.createAccessCookie(newAccessToken);
//                tokenSuccess(response, newAccessCookie, refreshCookie, jwtInfo);
//
//                chain.doFilter(request, response);
//            }
//            catch (ExpiredJwtException refreshTokenExpired)
//            {
//                // refreshToken도 만료되었다면 재로그인 유도
//                accessCookie = cookieUtil.getLogoutCookie(accessCookie);
//                refreshCookie = cookieUtil.getLogoutCookie(refreshCookie);
//                SecurityContextHolder.getContext().setAuthentication(null);
//                response.addCookie(accessCookie);
//                response.addCookie(refreshCookie);
//                tokenError(response, ErrorCode.RE_LOGIN_ERROR);
//            }
//            catch (Exception refreshTokenException)
//            {
//                // refreshToken에 뭔가 이상이 있다면 에러 리턴
//                log.error("** AUTHENTICATION FAILED : " + refreshTokenException);
//                tokenError(response, ErrorCode.INVALID_TOKEN_ERROR);
//            }
//        }
//        catch (Exception accessTokenException)
//        {
//            // accessToken이 애초에 잘못된 토큰이라면 에러 리턴
//            log.error("** AUTHENTICATION FAILED : " + accessTokenException);
//            tokenError(response, ErrorCode.INVALID_TOKEN_ERROR);
//        }
    }

    private Authentication getAuthenticationFromJwtInfo(JwtUtil.JwtInfo jwtInfo) {
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
    private void tokenSuccess(HttpServletResponse response, Cookie accessCookie, Cookie refreshCookie, JwtUtil.JwtInfo jwtInfo) {
        Authentication authentication = getAuthenticationFromJwtInfo(jwtInfo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipPathRequestMatcher.matches(request);
    }
}
