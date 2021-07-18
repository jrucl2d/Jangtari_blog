package com.yu.jangtari.UtilTest;

import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JwtAndCookieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
class CookieUtilTest {

    private CookieUtil cookieUtil;
    private JwtAndCookieInfo jwtAndCookieInfo;

    @BeforeEach
    void setUp()
    {
        jwtAndCookieInfo = new JwtAndCookieInfo();
        cookieUtil = new CookieUtil(jwtAndCookieInfo);
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
        assertEquals(jwtAndCookieInfo.getACCESS_TOKEN_VALID_TIME(), accessCookie.getMaxAge());
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
        assertEquals(jwtAndCookieInfo.getREFRESH_TOKEN_VALID_TIME(), refreshCookie.getMaxAge());
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
        Cookie gotAccessCookie = cookieUtil.getCookie(request, jwtAndCookieInfo.getACCESS_COOKIE_NAME());

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
        Cookie gotRefreshCookie = cookieUtil.getCookie(request, jwtAndCookieInfo.getREFRESH_COOKIE_NAME());

        // then
        assertEquals(gotRefreshCookie, refreshCookie);
    }

    @Test
    @DisplayName("없는 쿠키를 가져오려고 하면 null이 리턴됨")
    void getCookie_X()
    {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        Cookie accessCookie = cookieUtil.getCookie(request, jwtAndCookieInfo.getACCESS_COOKIE_NAME());
        Cookie refreshCookie = cookieUtil.getCookie(request, jwtAndCookieInfo.getREFRESH_COOKIE_NAME());

        // then
        assertNull(accessCookie);
        assertNull(refreshCookie);
    }
}