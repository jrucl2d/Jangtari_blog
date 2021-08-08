package com.yu.jangtari.util;

import com.yu.jangtari.security.jwt.JwtInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CookieUtil {
    private static final String ACCESS_COOKIE_NAME = "accessCookie";
    private static final String REFRESH_COOKIE_NAME = "refreshCookie";

    private static final int ACCESS_TOKEN_VALID_TIME = 2 * 60; // Access token 2분
    private static final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60; // Refresh token 1주일

    // =================================================================================================================
    // cookie 생성 관련 메소드
    // =================================================================================================================
    public List<Cookie> createCookies(JwtInfo jwtInfo) {
        return Arrays.asList(createAccessCookie(jwtInfo), createRefreshCookie(jwtInfo));
    }
    public Cookie createAccessCookie(JwtInfo jwtInfo) {
        return createCookie(jwtInfo, ACCESS_COOKIE_NAME, ACCESS_TOKEN_VALID_TIME);
    }
    public Cookie createRefreshCookie(JwtInfo jwtInfo) {
        return createCookie(jwtInfo, REFRESH_COOKIE_NAME, REFRESH_TOKEN_VALID_TIME);
    }
    private Cookie createCookie(JwtInfo jwtInfo, String cookieName, int expireTime) {
        String token = JwtUtil.createToken(jwtInfo, expireTime);
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true); // httpOnly 설정
        cookie.setMaxAge(Integer.MAX_VALUE);
//        cookie.setSecure(true); // https 적용할 것이므로 secure 설정
        cookie.setPath("/");
        return cookie;
    }
    // 로그아웃을 위한 cookie 생성
    public List<Cookie> getLogoutCookies() {
        return Arrays.asList(
            logoutCookie(ACCESS_COOKIE_NAME),
            logoutCookie(REFRESH_COOKIE_NAME)
        );
    }
    private Cookie logoutCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    // =================================================================================================================
    // cookie, jwt parsing 관련 메소드
    // =================================================================================================================
    public JwtInfo parseTokenOfAccessCookie(HttpServletRequest request) {
        return parseTokenOfCookie(request, ACCESS_COOKIE_NAME);
    }

    public JwtInfo parseTokenOfRefreshCookie(HttpServletRequest request) {
        return parseTokenOfCookie(request, REFRESH_COOKIE_NAME);

    }
    private JwtInfo parseTokenOfCookie(HttpServletRequest request, String cookieName) throws
        ExpiredJwtException
        , UnsupportedJwtException
        , MalformedJwtException
        , SignatureException
        , IllegalArgumentException
    {
        String token = getCookieValue(request, cookieName);
        return JwtUtil.parseJwt(token);
    }
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new IllegalArgumentException();
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(cookieName))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);
    }
}