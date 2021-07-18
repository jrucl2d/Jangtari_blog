package com.yu.jangtari.UtilTest;

import com.yu.jangtari.domain.RoleType;
import com.yu.jangtari.util.JWTUtil;
import com.yu.jangtari.util.JwtAndCookieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp()
    {
        JwtAndCookieInfo jwtAndCookieInfo = new JwtAndCookieInfo();
        jwtUtil = new JWTUtil(jwtAndCookieInfo);
    }

    @Test
    @DisplayName("access token을 정상적으로 생성")
    void createAccessToken()
    {
        // given
        String token = jwtUtil.createAccessToken("jangtari", RoleType.USER);

        // when

        // then
    }
}
