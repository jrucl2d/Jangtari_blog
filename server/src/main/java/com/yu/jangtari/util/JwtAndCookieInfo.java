package com.yu.jangtari.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtAndCookieInfo
{
    private static final int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000; // Access token 2분
    private static final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일
    private static final String JWT_SECRET_KEY = "tmp";
    private static final String ACCESS_COOKIE_NAME = "access_cookie_name";
    private static final String REFRESH_COOKIE_NAME = "refresh_cookie_name";
}
