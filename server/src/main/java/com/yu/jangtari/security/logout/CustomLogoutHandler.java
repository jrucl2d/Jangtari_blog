package com.yu.jangtari.security.logout;

import com.yu.jangtari.api.member.domain.RefreshToken;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * '/logout' 으로 오는 요청
 */
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("** 로그아웃 진행");
        String username = (String) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(null);
        if (username == null) return;
        refreshTokenRepository.delete(RefreshToken.builder().username(username).build());
    }
}
