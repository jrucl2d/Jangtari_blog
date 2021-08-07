package com.yu.jangtari.security;

import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.security.login.LoginFilter;
import com.yu.jangtari.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * loadUserByUsername의 결과는 {@link LoginFilter}의 successfulAuthentication로 전달됨
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return new CustomUserDetail(memberRepository.findByUsername(username).orElseThrow(NoSuchMemberException::new));
    }
}