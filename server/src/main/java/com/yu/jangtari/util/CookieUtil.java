package com.yu.jangtari.util;

import com.yu.jangtari.common.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    private static final String ACCESS_COOKIE_NAME = "accessCookie";
    private static final String REFRESH_COOKIE_NAME = "refreshCookie";

    private static final int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000; // Access token 2분
    private static final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일

    public Cookie createAccessCookie(String value) {
        return createCookie(value, ACCESS_COOKIE_NAME, ACCESS_TOKEN_VALID_TIME);
    }
    public Cookie createRefreshCookie(String value) {
        return createCookie(value, REFRESH_COOKIE_NAME, REFRESH_TOKEN_VALID_TIME);
    }
    public Cookie getLogoutCookie(Cookie cookie) {
        cookie.setMaxAge(0); // expirationTime을 0으로
        cookie.setPath("/"); // 모든 경로에서 삭제
        return cookie;
    }

    private Cookie createCookie(String value, String cookieName, int expireTime){
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true); // httpOnly로 설정
        cookie.setMaxAge(expireTime);
//        cookie.setSecure(true); // https를 적용할 것이므로 secure 설정
        cookie.setPath("/");
        return cookie;
    }

    public Cookie getAccessCookie(HttpServletRequest req) {
        return getCookie(req, ACCESS_COOKIE_NAME);
    }

    public Cookie getRefreshCookie(HttpServletRequest req) {
        return getCookie(req, REFRESH_COOKIE_NAME);
    }

    private Cookie getCookie(HttpServletRequest req, String cookieName) throws InvalidTokenException
    {
        final Cookie[] cookies = req.getCookies();
        if(cookies == null) throw new InvalidTokenException();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        throw new InvalidTokenException();
    }
}