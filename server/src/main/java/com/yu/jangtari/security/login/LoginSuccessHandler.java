package com.yu.jangtari.security.login;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.security.CustomUserDetail;
import com.yu.jangtari.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request
        , HttpServletResponse response
        , Authentication authentication) throws IOException, ServletException {

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member member = customUserDetail.getMember();
        log.info("** 로그인 성공 : " + member.getUsername());

        cookieUtil.createCookies(member).forEach(response::addCookie);
    }
}
