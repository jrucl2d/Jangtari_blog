package com.yu.jangtari.security;

import com.yu.jangtari.api.member.domain.Member;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class CustomUserDetail extends User {
    private static final String ROLE_PREFIX = "ROLE_";
    private final Member member;

    public CustomUserDetail(Member member){
        super(member.getUsername(), member.getPassword(), makeGrantedAuthorities(member.getRole().name()));
        this.member = member;
    }

    private static List<GrantedAuthority> makeGrantedAuthorities(String roleType){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + roleType));
        return list;
    }
}
