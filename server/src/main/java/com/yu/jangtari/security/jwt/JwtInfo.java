package com.yu.jangtari.security.jwt;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class JwtInfo {
    private final Long memberId;
    private final String username;
    private final String nickname;
    private final RoleType roleType;

    @Builder
    private JwtInfo(Long memberId, String username, String nickName, RoleType roleType)
    {
        this.memberId = memberId;
        this.username = username;
        this.nickname = nickName;
        this.roleType = roleType;
    }

    public static JwtInfo of(Member member) {
        return JwtInfo.builder()
            .memberId(member.getId())
            .username(member.getUsername())
            .nickName(member.getNickname())
            .roleType(member.getRole())
            .build();
    }

    public static JwtInfo of(Map<String, Object> claims) {
        Long memberId = Long.valueOf((String) claims.get("memberId"));
        String username = (String) claims.get("username");
        String nickname = (String) claims.get("nickname");
        String role = (String) claims.get("role");
        return JwtInfo.builder()
            .memberId(memberId)
            .username(username)
            .nickName(nickname)
            .roleType(RoleType.of(role))
            .build();
    }

    public String getAuthority() {
        return "ROLE_" + roleType.name();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtInfo jwtInfo = (JwtInfo) o;
        return Objects.equals(memberId, jwtInfo.memberId) && Objects.equals(username, jwtInfo.username) && Objects.equals(nickname, jwtInfo.nickname) && roleType == jwtInfo.roleType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(memberId, username, nickname, roleType);
    }
}
