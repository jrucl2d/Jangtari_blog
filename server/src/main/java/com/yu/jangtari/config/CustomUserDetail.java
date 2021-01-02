package com.yu.jangtari.config;

import com.yu.jangtari.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomUserDetail extends User {
    private static final String ROLE_PREFIX = "ROLE_";
    private Member member;

    public CustomUserDetail(Member member){
        super(member.getUsername(), member.getPassword(), makeGrantedAuthoriy(member.getRole().name()));
        this.member = member;
    }

    private static List<GrantedAuthority> makeGrantedAuthoriy(String roleType){
        List<GrantedAuthority> list = new ArrayList<>();

        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + roleType));
        return list;
    }
}
