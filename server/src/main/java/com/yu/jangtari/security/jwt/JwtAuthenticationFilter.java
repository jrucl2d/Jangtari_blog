package com.yu.jangtari.security.jwt;

import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
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
            JwtInfo jwtInfo = cookieUtil.parseTokenOfAccessCookie(request);
            setAuthentication(jwtInfo);
            Arrays.stream(request.getCookies()).forEach(response::addCookie);
        }
        catch (ExpiredJwtException accessTokenExpiredException)
        {
            // access token이 만료되었을 경우
            try
            {
                // refresh token이 정상적일 경우 access token 재발급
                JwtInfo jwtInfo = cookieUtil.parseTokenOfRefreshCookie(request);
                setAuthentication(jwtInfo);
                Arrays.stream(request.getCookies()).forEach(response::addCookie);
                response.addCookie(cookieUtil.createAccessCookie(jwtInfo)); // 재발급한 accessCookie
            }
            catch (Exception refreshTokenException)
            {
                // refresh token이 없거나 잘못되거나 만료되었을 경우
                log.error("JwtAuthenticationFilter refreshToken error : ", refreshTokenException);
            }
        }
        catch (Exception accessTokenException)
        {
            // access token이 없거나 잘못되었을 경우
            log.error("JwtAuthenticationFilter accessToken error : ", accessTokenException);
        }
        chain.doFilter(request, response);
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
