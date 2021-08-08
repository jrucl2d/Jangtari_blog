package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {
    private final JwtInfo jwtInfo = new JwtInfo("username", RoleType.USER);

    @Test
    @DisplayName("토큰이 정상적으로 생성됨")
    void createAccessToken()
    {
        // given
        // when
        String token = JwtUtil.createToken(jwtInfo, 10);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("토큰을 정상적을 parsing 가능")
    void parseJwt_O()
    {
        // given
        String token = JwtUtil.createToken(jwtInfo, 10);

        // when
        JwtInfo parseJwt = JwtUtil.parseJwt(token);

        // then
        assertThat(jwtInfo).isEqualTo(parseJwt);
    }

    @Test
    @DisplayName("만료된 토큰 parsing 할 시 ExpiredJwtException 발생")
    void parseJwt_X()
    {
        // given
        String token = JwtUtil.createToken(jwtInfo, 0);

        // when
        // then
        assertThrows(ExpiredJwtException.class, () -> JwtUtil.parseJwt(token));
    }

    @Test
    @DisplayName("이상한 토큰 parsing 할 시 MalformedJwtException 발생")
    void parseJwt_X1()
    {
        // given
        String token = "wlkgjwelkgjwelkgjwelgjwelkgjwe";

        // when
        // then
        assertThrows(MalformedJwtException.class, () -> JwtUtil.parseJwt(token));
    }

    @Test
    @DisplayName("토큰이 null, IllegalArgumentException 발생")
    void parseJwt_X2()
    {
        // given
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> JwtUtil.parseJwt(null));
    }
}