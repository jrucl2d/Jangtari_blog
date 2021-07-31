package com.yu.jangtari.util;

import com.yu.jangtari.common.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {
    private CookieUtil cookieUtil;

    @BeforeEach
    void setUp()
    {
        cookieUtil = new CookieUtil();
    }

    @Test
    @DisplayName("Access Cookie를 생성하면 Access Token만큼의 MAX AGE, token 값, /의 path를 가짐")
    void createAccessCookie()
    {
        // given
        String token = "jwt-token";

        // when
        Cookie accessCookie = cookieUtil.createAccessCookie(token);

        // then
        assertEquals(token, accessCookie.getValue());
        assertEquals("/", accessCookie.getPath());
        // assertTrue(accessCookie.getSecure());
    }

    @Test
    @DisplayName("Refresh Cookie를 생성하면 Refresh Token만큼의 MAX AGE, token 값, /의 path를 가짐")
    void createRefreshCookie()
    {
        // given
        String token = "jwt-token";

        // when
        Cookie refreshCookie = cookieUtil.createRefreshCookie(token);

        // then
        assertEquals(token, refreshCookie.getValue());
        assertEquals("/", refreshCookie.getPath());
        // assertTrue(accessCookie.getSecure());
    }

    @Test
    @DisplayName("Access Cookie로 가져왔을 때 잘 가져와짐")
    void getAccessCookie()
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie("jwt-token");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(accessCookie);

        // when
        Cookie gotAccessCookie = cookieUtil.getAccessCookie(request);

        // then
        assertEquals(gotAccessCookie, accessCookie);
    }

    @Test
    @DisplayName("Refresh Cookie로 가져왔을 때 잘 가져와짐")
    void getRefreshCookie()
    {
        // given
        Cookie refreshCookie = cookieUtil.createRefreshCookie("jwt-token");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(refreshCookie);

        // when
        Cookie gotRefreshCookie = cookieUtil.getRefreshCookie(request);

        // then
        assertEquals(gotRefreshCookie, refreshCookie);
    }

    @Test
    @DisplayName("없는 쿠키를 가져오려고 하면 InvalidTokenException 발생함")
    void getCookie_X()
    {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        // then
        assertThrows(InvalidTokenException.class, () -> cookieUtil.getAccessCookie(request));
        assertThrows(InvalidTokenException.class, () -> cookieUtil.getRefreshCookie(request));
    }
}