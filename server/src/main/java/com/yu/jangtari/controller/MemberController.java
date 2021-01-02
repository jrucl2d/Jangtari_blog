package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.config.JWTTokenProvider;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.RoleType;
import com.yu.jangtari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MemberController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<CustomResponse> join(@RequestBody MemberDTO.Add member) {
        Member newMember = new Member();
        newMember.setUsername(member.getUsername());
        newMember.setNickname(member.getNickname());
        newMember.setPassword(passwordEncoder.encode(member.getPassword()));
        newMember.setRole(RoleType.USER);

        long response = memberRepository.save(newMember).getId();
        return new ResponseEntity<>(new CustomResponse<>
                (null, response),
                HttpStatus.OK);
    }

}
