package com.yu.jangtari.util;

import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AuthUtilTest {
    @Test
    @DisplayName("AuthUtil 로부터 로그인 한 사용자의 정보를 가져올 수 있다.")
    void name() {
        // given
        JwtInfo jwtInfo = new JwtInfo("username", RoleType.USER);
        Collection<GrantedAuthority> authorities
            = Collections.singletonList(new SimpleGrantedAuthority(jwtInfo.getAuthority()));
        Authentication authentication
            = new UsernamePasswordAuthenticationToken(jwtInfo.getUsername(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        String userId = AuthUtil.getMemberId();

        // then
        assertThat(userId).isEqualTo(jwtInfo.getUsername());
    }
}