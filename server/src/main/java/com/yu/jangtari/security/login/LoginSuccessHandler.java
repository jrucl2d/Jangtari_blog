package com.yu.jangtari.security.login;

import com.yu.jangtari.api.member.domain.JwtToken;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.repository.JwtTokenRepository;
import com.yu.jangtari.security.CustomUserDetail;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
        HttpServletRequest request
        , HttpServletResponse response
        , Authentication authentication) throws IOException, ServletException {

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member member = customUserDetail.getMember();
        log.info("** 로그인 성공 : " + member.getUsername());

        String token = jwtUtil.createAccessToken(JwtInfo.of(member));
        String jwtHash = jwtUtil.createRefreshToken(token);
        jwtTokenRepository.save(
            JwtToken.builder()
                .jwtHash(token)
                .username(member.getUsername())
                .build());
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}
