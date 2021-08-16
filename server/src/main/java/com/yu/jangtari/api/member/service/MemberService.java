package com.yu.jangtari.api.member.service;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RefreshToken;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GoogleDriveUtil googleDriveUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getMemberByName(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member updateMember(MemberDto.Update memberDTO) {
        Member member = findOne(1L);
        member.updateMember(memberDTO.toURLContaining(googleDriveUtil));
        return member;
    }

    @Transactional
    public Member deleteMember(Long memberId) {
        Member member = findOne(memberId);
        member.delete();
        return member;
    }

    @Transactional
    public void join(MemberDto.Add memberDTO) {
        Member member = memberDTO.toEntity(passwordEncoder);
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATED_MEMBER_ERROR);
        }
    }

    public void logout() {
        log.info("** 로그아웃 진행");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        refreshTokenRepository.delete(RefreshToken.builder().username(username).build());
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
