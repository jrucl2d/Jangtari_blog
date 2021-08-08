package com.yu.jangtari.security.jwt;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"username", "roleType"})
public class JwtInfo
{
    private final String username;
    private final RoleType roleType;

    public static JwtInfo of(Member member)
    {
        return new JwtInfo(member.getUsername(), member.getRole());
    }
    public String getAuthority() {
        return "ROLE_" + roleType.name();
    }
}
