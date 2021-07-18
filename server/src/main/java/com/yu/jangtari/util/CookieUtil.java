package com.yu.jangtari.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    public final String ACCESS_COOKIE_NAME;
    public final String REFRESH_COOKIE_NAME;
    private final int ACCESS_TOKEN_VALID_TIME;
    private final int REFRESH_TOKEN_VALID_TIME;

    public CookieUtil(JwtAndCookieInfo jwtAndCookieInfo) {
        ACCESS_COOKIE_NAME = jwtAndCookieInfo.getACCESS_COOKIE_NAME();
        REFRESH_COOKIE_NAME = jwtAndCookieInfo.getREFRESH_COOKIE_NAME();
        ACCESS_TOKEN_VALID_TIME = jwtAndCookieInfo.getACCESS_TOKEN_VALID_TIME();
        REFRESH_TOKEN_VALID_TIME = jwtAndCookieInfo.getREFRESH_TOKEN_VALID_TIME();
    }

    public Cookie createAccessCookie(String value) {
        return createCookie(true, value);
    }
    public Cookie createRefreshCookie(String value) {
        return createCookie(false, value);
    }

    private Cookie createCookie(boolean isAccess, String value){
        final Cookie cookie = new Cookie(isAccess ? ACCESS_COOKIE_NAME : REFRESH_COOKIE_NAME, value);
        cookie.setHttpOnly(true); // httpOnly로 설정
        cookie.setMaxAge(isAccess ? ACCESS_TOKEN_VALID_TIME : REFRESH_TOKEN_VALID_TIME);
//        cookie.setSecure(true); // https를 적용할 것이므로 secure 설정
        cookie.setPath("/");
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies == null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
}