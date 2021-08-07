package com.yu.jangtari.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.api.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class ResponseUtil
{
    private final ObjectMapper objectMapper;
    private final CookieUtil cookieUtil;

    public void loginSuccess(HttpServletResponse response, Member member) {

    }
}
