package com.yu.jangtari.config;

import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return new CustomUserDetail(memberRepository.findByUsername(username).orElseThrow(NoSuchMemberException::new));
    }
}