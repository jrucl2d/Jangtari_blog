package com.yu.jangtari.service;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GoogleDriveUtil googleDriveUtil;

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
    }
    /**
     * soft delete을 구현하기 위해 service에서 dirty checking을 활용
     */
    public Member deleteMember(Long memberId) {
        Member member = findOne(memberId);
        member.getDeleteFlag().softDelete();
        return member;
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
}
