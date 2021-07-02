package com.yu.jangtari.config;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieUtil {
    public static final String ACCESS_COOKIE_NAME = "accessCookie";
    public static final String REFRESH_COOKIE_NAME = "refreshCookie";

    public Cookie createCookie(boolean isAccess, String value){
        final Cookie cookie = new Cookie(isAccess ? ACCESS_COOKIE_NAME : REFRESH_COOKIE_NAME, value);
        cookie.setHttpOnly(true); // httpOnly로 설정
        cookie.setMaxAge(isAccess ? JWTUtil.ACCESS_TOKEN_VALID_TIME : JWTUtil.REFRESH_TOKEN_VALID_TIME);
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