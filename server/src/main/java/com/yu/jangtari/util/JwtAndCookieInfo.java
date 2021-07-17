package com.yu.jangtari.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtAndCookieInfo
{
    private final int ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000; // Access token 2분
    private final int REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000; // Refresh token 1주일
    private  final String JWT_SECRET_KEY = "tmp";
    private final String ACCESS_COOKIE_NAME = "access_cookie_name";
    private final String REFRESH_COOKIE_NAME = "refresh_cookie_name";
}
