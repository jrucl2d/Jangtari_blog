package com.yu.jangtari.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.config.CookieUtil;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.config.JWTTokenProvider;
import com.yu.jangtari.config.RedisUtil;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.RoleType;
import com.yu.jangtari.repository.MemberRepository;
import com.yu.jangtari.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
public class MemberController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private MemberService memberService;

    @GetMapping("/jangtari")
    public ResponseEntity<CustomResponse> getInfo(){
        Optional<Member> member = memberRepository.findById(1L);
        if(!member.isPresent()){
            return new ResponseEntity<>(new CustomResponse(
                    new CustomError(new CustomException("그런사람 없음...", "장따리 찾기 실패")),null),
                    HttpStatus.BAD_REQUEST);
        }
        MemberDTO.Info response = new MemberDTO.Info();
        response.setNickname(member.get().getNickname());
        response.setIntroduce(member.get().getIntroduce());
        response.setPicture(member.get().getPicture());
        return new ResponseEntity<>(new CustomResponse(null, response),
                HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/admin/jangtari")
    public ResponseEntity<CustomResponse> updateInfo(@RequestPart("jangtari") String jangtariString,
                                                     @RequestPart("image") MultipartFile jangtariImage) throws GeneralSecurityException, IOException {
        return memberService.jangtariUpdate(jangtariString, jangtariImage);
    }
    @Transactional
    @PutMapping("/admin/jangtari/nimg")
    public ResponseEntity<CustomResponse> updateInfo2(@RequestPart("jangtari") String jangtariString) throws GeneralSecurityException, IOException {
        return memberService.jangtariUpdate(jangtariString, null);
    }
    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<CustomResponse> join(@RequestBody MemberDTO.Add member) {
        Member newMember = new Member();
        if(member.getUsername().substring(0, 5).equals("jang-")){
            newMember.setUsername(member.getUsername().substring(5));
            newMember.setNickname(member.getNickname());
            newMember.setPassword(passwordEncoder.encode(member.getPassword()));
            newMember.setRole(RoleType.ADMIN);
        } else{
            newMember.setUsername(member.getUsername());
            newMember.setNickname(member.getNickname());
            newMember.setPassword(passwordEncoder.encode(member.getPassword()));
            newMember.setRole(RoleType.USER);
        }

        try {
            memberRepository.save(newMember).getId();
            return new ResponseEntity<>(CustomResponse.OK(),
                    HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new CustomResponse(
                    new CustomError(new CustomException(e.getMessage(), "회원가입 실패")),null),
                    HttpStatus.BAD_REQUEST);
        }

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<CustomResponse> login(@RequestBody MemberDTO.Login member,
                                                HttpServletResponse res) {

        Optional<Member> member1 = memberRepository.findByUsername(member.getUsername());
        if(!member1.isPresent()){
            return new ResponseEntity<>(new CustomResponse(
                    new CustomError(new CustomException("회원 정보 없음", "로그인 실패")),null),
                    HttpStatus.UNAUTHORIZED);
        }

        if(!passwordEncoder.matches(member.getPassword(), member1.get().getPassword())){
            return new ResponseEntity<>(new CustomResponse(
                    new CustomError(new CustomException("비밀번호 오류", "로그인 실패")),null),
                    HttpStatus.UNAUTHORIZED);
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());

        Cookie accessCookie = cookieUtil.createCookie(jwtTokenProvider.ACCESS_TOKEN_STRING, accessToken);
        Cookie refreshCookie = cookieUtil.createCookie(jwtTokenProvider.REFRESH_TOKEN_STRING, refreshToken);

        redisUtil.setDataExpire(refreshToken, member.getUsername(), jwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        res.addCookie(accessCookie);
        res.addCookie(refreshCookie);
        HashMap<String, String> response = new HashMap<>();
        response.put(member1.get().getNickname(), member1.get().getRole().name());

        return new ResponseEntity<>(new CustomResponse<>
                (null, response),
                HttpStatus.OK);
    }

    @GetMapping("/signout")
    public ResponseEntity<CustomResponse> logout(HttpServletRequest request, HttpServletResponse response){
        // access 쿠키 삭제
        Cookie accessCookie = new Cookie("accesstest", null);
        accessCookie.setMaxAge(0); // expirationTime을 0으로
        accessCookie.setPath("/"); // 모든 경로에서 삭제
        response.addCookie(accessCookie);

        Cookie refreshCookie = cookieUtil.getCookie(request,jwtTokenProvider.REFRESH_TOKEN_STRING);
        String refreshToken = refreshCookie.getValue();
        redisUtil.deleteData(refreshToken);

        return new ResponseEntity<>(CustomResponse.OK(),
                HttpStatus.OK);
    }
}
