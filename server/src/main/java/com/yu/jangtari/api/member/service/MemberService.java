package com.yu.jangtari.api.member.service;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GoogleDriveUtil googleDriveUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getMemberByName(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member updateMember(MemberDto.Update memberDTO) {
        Member member = findOne(1L);
        member.updateMember(memberDTO.toURLContaining(googleDriveUtil));
        return member;
    }

    public Member deleteMember(Long memberId) {
        Member member = findOne(memberId);
        member.delete();
        return member;
    }

    public void join(MemberDto.Add memberDTO) {
        Member member = memberDTO.toEntity(passwordEncoder);
        memberRepository.save(member);
    }
}
