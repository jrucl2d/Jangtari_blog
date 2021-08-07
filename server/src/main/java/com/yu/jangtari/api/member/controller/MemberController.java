package com.yu.jangtari.api.member.controller;

import com.yu.jangtari.api.member.dto.MemberDTO;
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

    // About Jangtari
    @GetMapping("/jangtari")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDTO.Get findJangtari()
    {
        return MemberDTO.Get.of(memberService.findOne(1L));
    }

    @DeleteMapping("/member/{memberId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteMember(@PathVariable(value = "memberId") Long memberId)
    {
        memberService.deleteMember(memberId);
        return "OK";
    }

    @PostMapping(value = "/admin/jangtari", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDTO.Get updateJangtari(@Valid final MemberDTO.Update memberDTO)
    {
        return MemberDTO.Get.of(memberService.updateMember(memberDTO));
    }

    @PostMapping("/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String join(@RequestBody @Valid final MemberDTO.Add memberDTO)
    {
        memberService.join(memberDTO);
        return "OK";
    }
}