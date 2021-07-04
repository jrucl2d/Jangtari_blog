package com.yu.jangtari.config;

import com.yu.jangtari.repository.member.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// BasicAuthenticationFilter는 권한 인증이 필요한 경우에만 동작함
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private MemberRepository memberRepository;
    private CookieUtil cookieUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, CookieUtil cookieUtil) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilterInternal(request, response, chain);
    }
}
