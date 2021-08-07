package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CookieUtil {
    private final JwtUtil jwtUtil;

    private static final String ACCESS_COOKIE_NAME = "accessCookie";
    private static final String REFRESH_COOKIE_NAME = "refreshCookie";

    private static final int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000; // Access token 2분
    private static final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일

    public List<Cookie> createCookies(Member member) {
        return Arrays.asList(createAccessCookie(member), createRefreshCookie(member));
    }
    public Cookie createAccessCookie(Member member) {
        return createCookie(member, ACCESS_COOKIE_NAME, ACCESS_TOKEN_VALID_TIME);
    }
    public Cookie createRefreshCookie(Member member) {
        return createCookie(member, REFRESH_COOKIE_NAME, REFRESH_TOKEN_VALID_TIME);
    }
    public List<Cookie> getLogoutCookies() {
        List<Cookie> cookies = Arrays.asList(
            new Cookie(ACCESS_COOKIE_NAME, null),
            new Cookie(ACCESS_COOKIE_NAME, null)
        );
        return cookies.stream().map(cookie -> {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            return cookie;
        }).collect(Collectors.toList());
    }

    private Cookie createCookie(Member member, String cookieName, int expireTime) {
        JwtUtil.JwtInfo jwtInfo = JwtUtil.JwtInfo.of(member);
        String token = jwtUtil.createAccessToken(jwtInfo);
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true); // httpOnly로 설정
        cookie.setMaxAge(expireTime);
//        cookie.setSecure(true); // https를 적용할 것이므로 secure 설정
        cookie.setPath("/");
        return cookie;
    }

    public String getAccessTokenFromCookie(HttpServletRequest req) {
        Cookie cookie = getCookie(req, ACCESS_COOKIE_NAME);
        return cookie.getValue();
    }

    public String getRefreshTokenFromCookie(HttpServletRequest req) {
        Cookie cookie = getCookie(req, REFRESH_COOKIE_NAME);
        return cookie.getValue();
    }

    private Cookie getCookie(HttpServletRequest req, String cookieName) throws IllegalArgumentException {
        Cookie[] cookies = req.getCookies();
        if(cookies == null) throw new IllegalArgumentException();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        throw new IllegalArgumentException();
    }
}