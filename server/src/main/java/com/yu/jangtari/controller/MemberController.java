package com.yu.jangtari.controller;

import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // About Jangtari
    @GetMapping("/jangtari")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDTO.Get findJangtari() {
        return MemberDTO.Get.of(memberService.findOne(1L));
    }

    @DeleteMapping("/member/{memberId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteMember(@PathVariable(value = "memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return "OK";
    }

    @PostMapping(value = "/admin/jangtari", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDTO.Get updateJangtari(@Valid final MemberDTO.Update memberDTO) {
        return MemberDTO.Get.of(memberService.updateMember(memberDTO));
    }

    @PostMapping("/join")
    public String join(@RequestBody @Valid final MemberDTO.Add memberDTO) {
        memberService.join(memberDTO);
        return "OK";
    }
    
//    @PostMapping("/check")
//    public ResponseEntity<CustomResponse> check(@RequestBody MemberDTO.Check password, Principal principal){
//        String username = principal.getName();
//        Optional<Member> found = memberRepository.findByUsername(username);
//        if(!found.isPresent()){
//            return new ResponseEntity<>(new CustomResponse(
//                    new ErrorResponse(new CustomException("유저 존재하지 않음", "회원인증 실패")),null),
//                    HttpStatus.BAD_REQUEST);
//        }
//        String encodedPW = found.get().getPassword();
//
//        if(passwordEncoder.matches(password.getPassword(), encodedPW)){
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(new CustomResponse(
//                    new ErrorResponse(new CustomException("비밀번호 인증 오류", "회원인증 실패")),null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }


//    }
//    @PostMapping("/member")
//    public ResponseEntity<CustomResponse> memberUpdate(@RequestBody MemberDTO.Add member, HttpServletRequest request, HttpServletResponse response) {
//
//        try {
//            Optional<Member> member1 = memberRepository.findByUsername(member.getUsername());
//            if(!member1.isPresent()){
//                return new ResponseEntity<>(new CustomResponse(
//                        new ErrorResponse(new CustomException("회원 정보 없음", "회원 정보 업데이트 실패")),null),
//                        HttpStatus.BAD_REQUEST);
//            }
//            member1.get().setNickname(member.getNickname());
//            member1.get().setPassword(passwordEncoder.encode(member.getPassword()));
//            memberRepository.save(member1.get());
//
//            // 로그아웃 처리
//            Cookie accessCookie = new Cookie("accesstest", null);
//            accessCookie.setMaxAge(0); // expirationTime을 0으로
//            accessCookie.setPath("/"); // 모든 경로에서 삭제
//            response.addCookie(accessCookie);
//
//            Cookie refreshCookie = cookieUtil.getCookie(request,jwtTokenProvider.REFRESH_TOKEN_STRING);
//            String refreshToken = refreshCookie.getValue();
//            redisUtil.deleteData(refreshToken);
//
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.OK);
//        } catch (Exception e){
//            return new ResponseEntity<>(new CustomResponse(
//                    new ErrorResponse(new CustomException(e.getMessage(), "회원 정보 업데이트 실패")),null),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//    }
//    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity<CustomResponse> login(@RequestBody MemberDTO.Login member,
//                                                HttpServletResponse res) {
//
//        Optional<Member> member1 = memberRepository.findByUsername(member.getUsername());
//        if(!member1.isPresent()){
//            return new ResponseEntity<>(new CustomResponse(
//                    new ErrorResponse(new CustomException("회원 정보 없음", "로그인 실패")),null),
//                    HttpStatus.UNAUTHORIZED);
//        }
//
//        if(!passwordEncoder.matches(member.getPassword(), member1.get().getPassword())){
//            return new ResponseEntity<>(new CustomResponse(
//                    new ErrorResponse(new CustomException("비밀번호 오류", "로그인 실패")),null),
//                    HttpStatus.UNAUTHORIZED);
//        }
//        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
//        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());
//
//        Cookie accessCookie = cookieUtil.createCookie(jwtTokenProvider.ACCESS_TOKEN_STRING, accessToken);
//        Cookie refreshCookie = cookieUtil.createCookie(jwtTokenProvider.REFRESH_TOKEN_STRING, refreshToken);
//
//        redisUtil.setDataExpire(refreshToken, member.getUsername(), jwtTokenProvider.REFRESH_TOKEN_VALID_TIME);
//
//        res.addCookie(accessCookie);
//        res.addCookie(refreshCookie);
//        HashMap<String, String> response = new HashMap<>();
//        response.put(member1.get().getNickname(), member1.get().getRole().name());
//
//        return new ResponseEntity<>(new CustomResponse<>
//                (null, response),
//                HttpStatus.OK);
//    }
//
//    @GetMapping("/signout")
//    public ResponseEntity<CustomResponse> logout(HttpServletRequest request, HttpServletResponse response){
//        // access 쿠키 삭제
//        Cookie accessCookie = new Cookie("accesstest", null);
//        accessCookie.setMaxAge(0); // expirationTime을 0으로
//        accessCookie.setPath("/"); // 모든 경로에서 삭제
//        response.addCookie(accessCookie);
//
//        Cookie refreshCookie = cookieUtil.getCookie(request,jwtTokenProvider.REFRESH_TOKEN_STRING);
//        String refreshToken = refreshCookie.getValue();
//        redisUtil.deleteData(refreshToken);
//
//        return new ResponseEntity<>(CustomResponse.OK(),
//                HttpStatus.OK);
//    }
}
