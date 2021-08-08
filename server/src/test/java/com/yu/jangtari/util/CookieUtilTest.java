package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CookieUtilTest {
    private final CookieUtil cookieUtil = new CookieUtil();
    private final JwtInfo jwtInfo = new JwtInfo("username", RoleType.USER);

    @Test
    @DisplayName("accessCookie, refreshCookie 생성하면 정상적으로 생성됨")
    void createAccessCookie()
    {
        // given
        // when
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtInfo);
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtInfo);

        // then
        assertThat(accessCookie.getValue()).isNotNull();
        assertThat(refreshCookie.getValue()).isNotNull();
    }

    @Test
    @DisplayName("cookie 사용자 정보를 잘 가져올 수 있음")
    void getAccessCookie()
    {
        // given
        Cookie accessCookie = cookieUtil.createAccessCookie(jwtInfo);
        Cookie refreshCookie = cookieUtil.createRefreshCookie(jwtInfo);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(accessCookie, refreshCookie);

        // when=
        JwtInfo accessInfo = cookieUtil.parseTokenOfAccessCookie(request);
        JwtInfo refreshInfo = cookieUtil.parseTokenOfRefreshCookie(request);

        // then
        assertThat(jwtInfo).isEqualTo(accessInfo);
        assertThat(accessInfo).isEqualTo(refreshInfo);
    }

    @Test
    @DisplayName("로그아웃을 위한 MaxAge 0인 쿠키를 생성함")
    void getLogOutCookie()
    {
        // given
        // when
        List<Cookie> logoutCookies = cookieUtil.getLogoutCookies();

        // then
        logoutCookies.forEach(
            cookie -> {
                assertThat(cookie.getPath()).isEqualTo("/");
                assertThat(cookie.getMaxAge()).isZero();
            }
        );
    }

    @Test
    @DisplayName("request 쿠키가 없으면 IllegalArgumentException 발생")
    void getCookie_X()
    {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> cookieUtil.parseTokenOfAccessCookie(request));
        assertThrows(IllegalArgumentException.class, () -> cookieUtil.parseTokenOfRefreshCookie(request));
    }

    @Test
    @DisplayName("request 쿠키가 있지만 jwt token null, IllegalArgumentException 발생")
    void getCookie_X1()
    {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<Cookie> cookies = Arrays.asList(new Cookie("accessCookie", null), new Cookie("refreshCookie", null));
        request.setCookies(cookies.get(0), cookies.get(1));

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> cookieUtil.parseTokenOfAccessCookie(request));
        assertThrows(IllegalArgumentException.class, () -> cookieUtil.parseTokenOfRefreshCookie(request));
    }

    @Test
    @DisplayName("token expired, ExpiredJwtException 발생")
    void getCookie_X3()
    {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = JwtUtil.createToken(jwtInfo, 0);
        List<Cookie> cookies = Arrays.asList(new Cookie("accessCookie", token), new Cookie("refreshCookie", token));
        request.setCookies(cookies.get(0), cookies.get(1));

        // when
        // then
        assertThrows(ExpiredJwtException.class, () -> cookieUtil.parseTokenOfAccessCookie(request));
        assertThrows(ExpiredJwtException.class, () -> cookieUtil.parseTokenOfRefreshCookie(request));
    }

    @Test
    @DisplayName("token malformed, MalformedJwtException 발생")
    void getCookie_X4()
    {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "welkgewlkgje.wlkgjewklgjlwekgjl.kwejglwekjglwekjglwkejg";
        List<Cookie> cookies = Arrays.asList(new Cookie("accessCookie", token), new Cookie("refreshCookie", token));
        request.setCookies(cookies.get(0), cookies.get(1));

        // when
        // then
        assertThrows(MalformedJwtException.class, () -> cookieUtil.parseTokenOfAccessCookie(request));
        assertThrows(MalformedJwtException.class, () -> cookieUtil.parseTokenOfRefreshCookie(request));
    }
}