package com.yu.jangtari.api.member.service;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RefreshToken;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GoogleDriveUtil googleDriveUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public MemberDto.Get getJangtari() {
        return MemberDto.Get.of(getOne(1L));
    }

    @Transactional(readOnly = true)
    public Member getOne(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_ERROR));
    }

    public MemberDto.Get updateMember(MemberDto.Update memberDto, MultipartFile picture) {
        Member member = getOne(memberDto.getId());
        return MemberDto.Get.of(member.updateMember(memberDto.toUrlDto(googleDriveUtil, picture)));
    }

    public void deleteMember(Long memberId) {
        Member member = getOne(memberId);
        member.delete();
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
        JwtInfo jwtInfo = (JwtInfo) authentication.getPrincipal();
        refreshTokenRepository.delete(RefreshToken.builder().username(jwtInfo.getUsername()).build());
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
