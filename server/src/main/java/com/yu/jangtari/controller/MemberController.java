package com.yu.jangtari.controller;

import com.yu.jangtari.common.CustomError;
import com.yu.jangtari.common.CustomException;
import com.yu.jangtari.common.CustomResponse;
import com.yu.jangtari.config.CookieUtil;
import com.yu.jangtari.config.JWTTokenProvider;
import com.yu.jangtari.config.RedisUtil;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.RoleType;
import com.yu.jangtari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/informationOfJangTtari")
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
    @PostMapping("/admin/informationOfJangTtariUpdate")
    public ResponseEntity<CustomResponse> updateInfo(@RequestBody MemberDTO.Info newInfo){
        Optional<Member> member = memberRepository.findById(1L);
        if(!member.isPresent()){
            return new ResponseEntity<>(new CustomResponse(
                    new CustomError(new CustomException("그런사람 없음...", "장따리 찾기 실패")),null),
                    HttpStatus.BAD_REQUEST);
        }
        member.get().setNickname(newInfo.getNickname());
        member.get().setIntroduce(newInfo.getIntroduce());
        member.get().setPicture(newInfo.getPicture());
        memberRepository.save(member.get());

        return new ResponseEntity<>(CustomResponse.OK(),
                HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<CustomResponse> join(@RequestBody MemberDTO.Add member) {
        Member newMember = new Member();
        newMember.setUsername(member.getUsername());
        newMember.setNickname(member.getNickname());
        newMember.setPassword(passwordEncoder.encode(member.getPassword()));
        newMember.setRole(RoleType.USER);

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
                                                HttpServletRequest req,
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

        return new ResponseEntity<>(new CustomResponse<>
                (null, member1.get().getRole()),
                HttpStatus.OK);
    }

    @GetMapping("/signout")
    public ResponseEntity<CustomResponse> logout(HttpServletRequest request){
        Cookie refreshCookie = cookieUtil.getCookie(request,jwtTokenProvider.REFRESH_TOKEN_STRING);
        String refreshToken = refreshCookie.getValue();
        redisUtil.deleteData(refreshToken);

        return new ResponseEntity<>(CustomResponse.OK(),
                HttpStatus.OK);
    }
}
