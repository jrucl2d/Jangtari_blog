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
    private final SkipPathRequestMatcher skipPathRequestMatcher;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager
        , SkipPathRequestMatcher skipPathRequestMatcher
        , RefreshTokenRepository refreshTokenRepository) {

        super(authenticationManager);
        this.skipPathRequestMatcher = skipPathRequestMatcher;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String refreshToken = request.getHeader(JwtUtil.REFRESH_TOKEN);
        if (refreshToken == null) {
            accessTokenTask(request, response, chain);
        } else {
            refreshTokenTask(request, response, chain);
        }
    }

    private void accessTokenTask(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            JwtInfo jwtInfo = JwtUtil.decodeAccessToken(accessToken);
            setAuthentication(jwtInfo);
        } catch (ExpiredJwtException e) {
            log.error("JwtAuthenticationFilter 에러 발생 : ", e);
            failureTask(response, new AuthException("access token 만료되었습니다.", ErrorCode.JWT_TIMEOUT_ERROR));
            return;
        } catch (Exception e) {
            log.error("JwtAuthenticationFilter 에러 발생 : ", e);
            failureTask(response, new AuthException("access token decoding 중에 에러가 발생했습니다.", ErrorCode.JWT_VALIDATION_ERROR));
            return;
        }
        chain.doFilter(request, response);
    }

    private void refreshTokenTask(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String refreshToken = request.getHeader(JwtUtil.REFRESH_TOKEN);
            JwtInfo jwtInfo = JwtUtil.decodeRefreshToken(refreshToken);
            String username = jwtInfo.getUsername();

            RefreshToken savedToken = refreshTokenRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("refresh token 존재하지 않습니다."));
            if (!refreshToken.equals(savedToken.getRefreshToken())) throw new IllegalArgumentException("db에 저장된 refreshToken과 다릅니다.");

            String newAccessToken = JwtUtil.createAccessToken(jwtInfo);
            response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
            setAuthentication(jwtInfo);
        } catch (ExpiredJwtException e) {
            log.error("JwtAuthenticationFilter 에러 발생 : ", e);
            failureTask(response, new AuthException("refresh token 만료되었습니다.", ErrorCode.JWT_TIMEOUT_ERROR));
            return;
        } catch (AuthException e) {
            log.error("JwtAuthenticationFilter 에러 발생 : ", e);
            failureTask(response, new AuthException("refresh token 안의 access token parsing 중에 에러가 발생했습니다.", ErrorCode.JWT_VALIDATION_ERROR));
            return;
        } catch (Exception e) {
            log.error("JwtAuthenticationFilter 에러 발생 : ", e);
            failureTask(response, new AuthException("refresh token decoding 중에 에러가 발생했습니다.", ErrorCode.JWT_VALIDATION_ERROR));
            return;
        }
        chain.doFilter(request, response);
    }

    private void failureTask(HttpServletResponse response, AuthException e) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        ResponseUtil.doResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
    }

    private void setAuthentication(JwtInfo jwtInfo) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtInfo
            , null
            , Collections.singletonList(new SimpleGrantedAuthority(jwtInfo.getAuthority())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipPathRequestMatcher.matches(request);
    }
}
