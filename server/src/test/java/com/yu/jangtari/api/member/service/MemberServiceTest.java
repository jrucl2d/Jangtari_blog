package com.yu.jangtari.api.member.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.testHelper.PictureFileUtil;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberServiceTest extends ServiceTest
{
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private GoogleDriveUtil googleDriveUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private Member admin;
    private Member user;

    @BeforeEach
    void setUp() {
        admin = Member.builder()
            .username("username")
            .nickname("nickname")
            .introduce("introduce")
            .password("password").picture("picture").build();
        user = Member.builder()
            .username("username")
            .nickname("nickname")
            .introduce("introduce")
            .password("password").picture("picture").build();
    }

    @Test
    @DisplayName("member의 정보 업데이트가 정상적으로 진행됨")
    void updateMember1()
    {
        // given
        MemberDto.Update memDTO = MemberDto.Update.builder().username("username").nickname("newNick").introduce("newIntro").build();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(admin));
        given(googleDriveUtil.fileToURL(any(), any())).willReturn("newPic");

        // when
        MemberDto.Get updatedMember = memberService.updateMember(memDTO, PictureFileUtil.createOne("pic"));

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
        BusinessException e = assertThrows(BusinessException.class, () -> memberService.deleteMember("haha"));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("member를 정상적으로 삭제")
    void deleteMember()
    {
        // given
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        // when
        memberService.deleteMember(user.getUsername());

        // then
        verify(memberRepository, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("회원가입이 정상적으로 성공함")
    void join()
    {
        // given
        MemberDto.Add memDTO = MemberDto.Add.builder().username("username").nickname("nickname").password("password").build();
        given(passwordEncoder.encode(anyString())).willReturn("encoded");
        given(memberRepository.save(any())).willReturn(admin);

        // when
        memberService.join(memDTO);

        // then
        verify(memberRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("이미 가입된 username으로 회원가입 하려하면 BusinessException 발생")
    void join_X()
    {
        // given
        MemberDto.Add memDto = MemberDto.Add.builder().username("username").nickname("nickname").password("password").build();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(Member.builder()
            .username("username")
            .nickname("nickname")
            .password("password")
            .build()));

        // when
        // then
        BusinessException e = assertThrows(BusinessException.class, () -> memberService.join(memDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_MEMBER_ERROR);
    }

    @Test
    @DisplayName("정상적으로 로그아웃 가능")
    void logout_O()
    {
        // given
        JwtInfo jwtInfo = JwtInfo.builder()
            .username("username")
            .nickName("nick")
            .roleType(RoleType.USER)
            .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtInfo, null, null));
        doNothing().when(refreshTokenRepository).delete(any());

        // when
        memberService.logout();

        // then
        verify(refreshTokenRepository, times(1)).delete(any());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}