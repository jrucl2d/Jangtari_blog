package com.yu.jangtari.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.yu.jangtari.common.ErrorResponse;
import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GoogleDriveUtil googleDriveUtil;

    /**
     * soft delete을 구현하기 위해 service에서 dirty checking을 활용
     */
    public Member deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NoSuchMemberException());
        member.getDeleteFlag().softDelete();
        return member;
    }

//    public ResponseEntity<CustomResponse> jangtariUpdate(String jangtariString, MultipartFile jangtariImage) throws IOException, GeneralSecurityException {
//        Optional<Member> memberO = memberRepository.findById(1L);
//        if(!memberO.isPresent()){
//            return new ResponseEntity<>(new CustomResponse(
//                    new ErrorResponse(new CustomException("그런사람 없음...", "장따리 찾기 실패")),null),
//                    HttpStatus.BAD_REQUEST);
//        }
//        Member member = memberO.get();
//        MemberDTO.PutInfo theMember = new ObjectMapper().readValue(jangtariString, MemberDTO.PutInfo.class);
//
//
//        member.setNickname(theMember.getNickname());
//        member.setIntroduce(theMember.getIntroduce());
//        if(jangtariImage != null){
//            Drive drive = googleDriveUtil.getDrive();
//            File file = new File();
//            file.setName(googleDriveUtil.getPictureName(jangtariImage.getName()));
//            file.setParents(Collections.singletonList(googleDriveUtil.JANGTARI_FOLDER));
//            java.io.File tmpFile = googleDriveUtil.convert(jangtariImage);
//            FileContent content = new FileContent("image/jpeg", tmpFile);
//            File uploadedFile = drive.files().create(file, content).setFields("id").execute();
//            tmpFile.delete();
//            String fileRef = googleDriveUtil.FILE_REF + uploadedFile.getId();
//            member.setPicture(fileRef);
//        }
//        memberRepository.save(member);
//        return new ResponseEntity<>(CustomResponse.OK(),
//                HttpStatus.OK);
//    }
}
