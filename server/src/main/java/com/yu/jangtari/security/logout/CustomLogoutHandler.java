package com.yu.jangtari.security.logout;

import com.yu.jangtari.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * '/logout' 으로 오는 요청에서 accessCookie 와 refreshCookie, SecurityContext 안의 Authentication 을 제거
 */
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler
{
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        log.info("** 로그아웃 진행");
        SecurityContextHolder.getContext().setAuthentication(null);
//        cookieUtil.getLogoutCookies().forEach(response::addCookie);
    }
}
