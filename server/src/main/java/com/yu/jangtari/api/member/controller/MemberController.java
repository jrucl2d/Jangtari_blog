package com.yu.jangtari.api.member.controller;

import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController
{

    private final MemberService memberService;

    @GetMapping("/jangtari")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Get findJangtari()
    {
        return MemberDto.Get.of(memberService.getOne(1L));
    }

    @DeleteMapping("/member/{memberId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteMember(@PathVariable(value = "memberId") Long memberId)
    {
        memberService.deleteMember(memberId);
        return "회원 삭제 성공";
    }

    @PostMapping(value = "/admin/jangtari", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Get updateJangtari(@Valid final MemberDto.Update memberDTO)
    {
        return MemberDto.Get.of(memberService.updateMember(memberDTO));
    }

    @PostMapping("/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String join(@RequestBody @Valid final MemberDto.Add memberDTO)
    {
        memberService.join(memberDTO);
        return "회원가입 성공";
    }

    @GetMapping("/user/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public String logout()
    {
        memberService.logout();
        return "로그아웃 성공";
    }
}