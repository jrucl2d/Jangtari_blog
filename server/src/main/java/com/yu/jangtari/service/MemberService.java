package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.DuplicateUserException;
import com.yu.jangtari.common.exception.JangtariDeleteError;
import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.util.GoogleDriveUtil;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public Member addMember(MemberDTO.Add memberDTO) {
        return memberRepository.save(memberDTO.toEntity());
    }

    public Member updateMember(MemberDTO.Update memberDTO) {
        final Member member = findOne(1L);
        member.updateNickNameAndIntoduce(memberDTO);
        updatePictureToMemberIfExist(member, memberDTO.getPicture());
        return member;
    }

    private void updatePictureToMemberIfExist(Member member, MultipartFile pictureFile) {
        if (pictureFile == null) return;
        String pictureURL = googleDriveUtil.fileToURL(pictureFile, GDFolder.JANGTARI);
        member.initPicture(pictureURL);
    }

    // 1번 유저, Jangtari는 삭제 불가능하다는 요구사항 추가
    public Member deleteMember(Long memberId) {
        if (memberId == 1L) throw new JangtariDeleteError();
        final Member member = findOne(memberId);
        member.getDeleteFlag().softDelete();
        return member;
    }

    public void join(MemberDTO.Add memberDTO) {
        checkUserDuplicate(memberDTO.getUsername());
        final Member member = Member.builder()
                .username(memberDTO.getUsername())
                .nickname(memberDTO.getNickname())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .build();
        memberRepository.save(member);
    }
    private void checkUserDuplicate(final String username) {
        if (memberRepository.findByUsername(username).isPresent()) throw new DuplicateUserException();
    }
}
