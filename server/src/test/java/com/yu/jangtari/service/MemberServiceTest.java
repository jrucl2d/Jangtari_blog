package com.yu.jangtari.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.exception.JangtariDeleteError;
import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberServiceTest extends ServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private GoogleDriveUtil googleDriveUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .username("username")
            .nickname("nickname")
            .introduce("introduce")
            .password("password").picture("picture").build();
    }

    @Test
    @DisplayName("이름으로 member 찾아왔을 때 없으면 NoSuchMemberException 발생")
    void getMemberByName() {
        // given
        // when
        given(memberRepository.findByUsername(any())).willReturn(Optional.empty());

        // then
        assertThrows(NoSuchMemberException.class, () -> memberService.getMemberByName("jang"));
    }

    @Test
    @DisplayName("사진 제외하고 member의 nickname, introduce 업데이트")
    void updateMember()
    {
        // given
        MemberDto.Update memDTO = MemberDto.Update.builder().nickname("newNick").introduce("newIntro").build();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(googleDriveUtil.fileToURL(any(), any())).willReturn(null);

        // when
        Member updatedMember = memberService.updateMember(memDTO);

        // then
        assertEquals("newNick", updatedMember.getNickname());
        assertEquals("newIntro", updatedMember.getIntroduce());
        assertEquals("picture", updatedMember.getPicture());
    }

    @Test
    @DisplayName("사진 포함하여 member의 정보 업데이트")
    void updateMember1()
    {
        // given
        MemberDto.Update memDTO = MemberDto.Update.builder().nickname("newNick").introduce("newIntro").build();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(googleDriveUtil.fileToURL(any(), any())).willReturn("newPic");

        // when
        Member updatedMember = memberService.updateMember(memDTO);

        // then
        assertEquals("newNick", updatedMember.getNickname());
        assertEquals("newIntro", updatedMember.getIntroduce());
        assertEquals("newPic", updatedMember.getPicture());
    }

    @Test
    @DisplayName("1번 회원 Jangtari는 삭제 불가능")
    void deleteMember_X()
    {
        // given
        // when
        // then
        assertThrows(JangtariDeleteError.class, () -> memberService.deleteMember(1L));
    }

    @Test
    @DisplayName("member를 정상적으로 삭제")
    void deleteMember()
    {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        Member deletedMember = memberService.deleteMember(2L);

        // then
        assertTrue(deletedMember.getDeleteFlag().isDeleteFlag());
    }

    @Test
    @DisplayName("회원가입이 정상적으로 성공함")
    void join()
    {
        // given
        MemberDto.Add memDTO = MemberDto.Add.builder().username("username").nickname("nickname").password("password").build();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("encoded");
        given(memberRepository.save(any())).willReturn(member);

        // when
        memberService.join(memDTO);

        // then
        verify(memberRepository, times(1)).findByUsername(anyString());
        verify(memberRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("이미 가입된 username으로 회원가입 하려하면 BusinessException 발생")
    void join_X()
    {
        // given
        MemberDto.Add memDTO = MemberDto.Add.builder().username("username").nickname("nickname").password("password").build();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        // when
        // then
        assertThrows(BusinessException.class, () -> memberService.join(memDTO));

    }
}