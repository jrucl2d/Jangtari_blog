package com.yu.jangtari.api.member.service;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.JangtariDeleteError;
import com.yu.jangtari.common.exception.NoSuchMemberException;
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
        return memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
    }

    @Transactional(readOnly = true)
    public Member getMemberByName(String username) {
        return memberRepository.findByUsername(username).orElseThrow(NoSuchMemberException::new);
    }

    public Member updateMember(MemberDto.Update memberDTO) {
        Member member = findOne(1L);
        String pictureURL = googleDriveUtil.fileToURL(memberDTO.getPicture(), GDFolder.JANGTARI);
        memberDTO.setPictureURL(pictureURL);
        member.updateMember(memberDTO);
        return member;
    }

    // 1번 유저, Jangtari는 삭제 불가능하다는 요구사항 추가
    public Member deleteMember(Long memberId) {
        if (memberId == 1L) throw new JangtariDeleteError();
        Member member = findOne(memberId);
        member.getDeleteFlag().softDelete();
        return member;
    }

    public Member join(MemberDto.Add memberDTO) {
        checkUserDuplicate(memberDTO.getUsername());
        Member member = memberDTO.toEntity(passwordEncoder);
        return memberRepository.save(member);
    }
    private void checkUserDuplicate(String username) {
        if (memberRepository.findByUsername(username).isPresent())
            throw new BusinessException("이미 존재하는 아이디입니다.", ErrorCode.JOIN_ERROR);
    }
}
