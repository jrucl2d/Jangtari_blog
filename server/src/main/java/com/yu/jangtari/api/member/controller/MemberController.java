package com.yu.jangtari.api.member.controller;

import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController
{
    private final MemberService memberService;

    @GetMapping("/jangtari")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Get findJangtari() {
        return memberService.getJangtari();
    }

    @DeleteMapping("/member/{username}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteMember(@PathVariable(value = "username") String username) {
        memberService.deleteMember(username);
    }

    @PutMapping(value = "/admin/jangtari")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Get updateMember(
        @Valid MemberDto.Update memberDTO) {
        return memberService.updateMember(memberDTO);
    }

    // TODO : 사진 업로드 필요
    @PostMapping(value = "/admin/jangtari/picture")
    @ResponseStatus(value = HttpStatus.OK)
    public String uploadPicture(@RequestParam(name = "picture") MultipartFile picture) {
        return null;
    }

    @PutMapping(value = "/user")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Get updateUserMember(
            @Valid MemberDto.Update memberDTO
    ) {
        return memberService.updateMember(memberDTO);
    }

    @PostMapping("/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String join(@RequestBody @Valid MemberDto.Add memberDTO) {
        memberService.join(memberDTO);
        return "회원가입 성공";
    }

    @GetMapping("/user/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public String logout() {
        memberService.logout();
        return "로그아웃 성공";
    }
}