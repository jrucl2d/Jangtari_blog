package com.yu.jangtari.security.jwt;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId", "username", "roleType"})
public class JwtInfo
{
    private final Long memberId;
    private final String username;
    private final RoleType roleType;

    public static JwtInfo of(Member member) {
        return new JwtInfo(member.getId(), member.getUsername(), member.getRole());
    }

    public static JwtInfo of(Map<String, Object> claims) {
        Long memberId = Long.valueOf((String) claims.get("memberId"));
        String username = (String) claims.get("username");
        String role = (String) claims.get("role");
        return new JwtInfo(memberId, username, RoleType.of(role));
    }

    public String getAuthority() {
        return "ROLE_" + roleType.name();
    }
}
