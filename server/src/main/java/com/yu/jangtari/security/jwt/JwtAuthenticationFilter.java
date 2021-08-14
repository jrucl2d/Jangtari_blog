package com.yu.jangtari.security.jwt;

import com.yu.jangtari.util.JwtUtil;
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
import java.util.Collections;

/**
 * https://programmersought.com/article/5941819063/
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final SkipPathRequestMatcher skipPathRequestMatcher;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, SkipPathRequestMatcher skipPathRequestMatcher) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.skipPathRequestMatcher = skipPathRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {

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
