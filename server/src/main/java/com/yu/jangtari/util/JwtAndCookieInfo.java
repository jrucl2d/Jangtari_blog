package com.yu.jangtari.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtAndCookieInfo
{
    private final int accessTokenValidTime = 2 * 60 * 1000; // Access token 2분
    private final int refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일
    private final String jwtSecretKey = "tmp";
    private final String accessCookieName = "access_cookie_name";
    private final String refreshCookieName = "refresh_cookie_name";
}
