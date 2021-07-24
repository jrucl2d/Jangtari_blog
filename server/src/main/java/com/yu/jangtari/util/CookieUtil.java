package com.yu.jangtari.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    public final String accessCookieName;
    public final String refreshCookieName;
    private final int accessTokenValidTime;
    private final int refreshTokenValidTime;

    public CookieUtil(JwtAndCookieInfo jwtAndCookieInfo) {
        accessCookieName = jwtAndCookieInfo.getAccessCookieName();
        refreshCookieName = jwtAndCookieInfo.getRefreshCookieName();
        accessTokenValidTime = jwtAndCookieInfo.getAccessTokenValidTime();
        refreshTokenValidTime = jwtAndCookieInfo.getRefreshTokenValidTime();
    }

    public Cookie createAccessCookie(String value) {
        return createCookie(true, value);
    }
    public Cookie createRefreshCookie(String value) {
        return createCookie(false, value);
    }

    private Cookie createCookie(boolean isAccess, String value){
        final Cookie cookie = new Cookie(isAccess ? accessCookieName : refreshCookieName, value);
        cookie.setHttpOnly(true); // httpOnly로 설정
        cookie.setMaxAge(isAccess ? accessTokenValidTime : refreshTokenValidTime);
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