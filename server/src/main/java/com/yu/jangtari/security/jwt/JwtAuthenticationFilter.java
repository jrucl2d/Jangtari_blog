package com.yu.jangtari.security.jwt;

import com.yu.jangtari.api.member.domain.RefreshToken;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.AuthException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.ErrorResponse;
import com.yu.jangtari.util.JwtUtil;
import com.yu.jangtari.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final SkipPathRequestMatcher skipPathRequestMatcher;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager
        , JwtUtil jwtUtil, SkipPathRequestMatcher skipPathRequestMatcher
        , RefreshTokenRepository refreshTokenRepository) {

        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.skipPathRequestMatcher = skipPathRequestMatcher;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String refreshToken = request.getHeader(JwtUtil.REFRESHTOKEN);
        if (refreshToken == null) {
            refreshTokenTask(request, response, chain);
        } else {
            accessTokenTask(request, response, chain);
        }
        chain.doFilter(request, response);
    }

    private void accessTokenTask(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            JwtInfo jwtInfo = jwtUtil.decodeAccessToken(accessToken);
            setAuthentication(jwtInfo);
        } catch (ExpiredJwtException e) {
            failureTask(response, new AuthException("access token 만료되었습니다.", ErrorCode.JWT_TIMEOUT_ERROR));
            return;
        } catch (Exception e) {
            failureTask(response, new AuthException("access token parsing 중에 에러가 발생했습니다.", ErrorCode.JWT_VALIDATION_ERROR));
            return;
        }
        chain.doFilter(request, response);
    }

    private void refreshTokenTask(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String refreshToken = request.getHeader(JwtUtil.REFRESHTOKEN);
            JwtInfo jwtInfo = jwtUtil.decodeRefreshToken(refreshToken);
            String username = jwtInfo.getUsername();

            RefreshToken savedToken = refreshTokenRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("refresh token 존재하지 않습니다."));
            if (refreshToken.equals(savedToken.getRefreshToken())) throw new IllegalArgumentException("refresh token 올바르지 않습니다.");

            setAuthentication(jwtInfo);
        } catch (ExpiredJwtException e) {
            failureTask(response, new AuthException("refresh token 만료되었습니다.", ErrorCode.JWT_TIMEOUT_ERROR));
            return;
        } catch (AuthException e) {
            failureTask(response, new AuthException("refresh token 안의 access token parsing 중에 에러가 발생했습니다.", ErrorCode.JWT_VALIDATION_ERROR));
            return;
        } catch (Exception e) {
            failureTask(response, new AuthException("refresh token parsing 중에 에러가 발생했습니다.", ErrorCode.JWT_VALIDATION_ERROR));
            return;
        }
        chain.doFilter(request, response);
    }

    private void failureTask(HttpServletResponse response, AuthException e) throws IOException {
        log.error("JwtAuthenticationFilter 에러 발생 : " + e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        ResponseUtil.doResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
    }

    private void setAuthentication(JwtInfo jwtInfo) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtInfo.getUsername()
            , null
            , Collections.singletonList(new SimpleGrantedAuthority(jwtInfo.getAuthority())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipPathRequestMatcher.matches(request);
    }
}
